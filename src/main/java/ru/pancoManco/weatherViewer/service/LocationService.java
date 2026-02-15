package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.context.UserContextHolder;
import ru.pancoManco.weatherViewer.dto.LocationRequestDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;

    public List<OpenWeatherCityResponseDto> getAllLocationForUser() {
        User user = UserContextHolder.get();
        List<Location> locations = locationRepository.getAllUserLocations(user);
        List<OpenWeatherCityResponseDto> result = new ArrayList<>();
        for (Location location : locations) {
            OpenWeatherCityResponseDto dto =
                    openWeatherService.getWeatherByCoordinates(
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getName()
                    );
            dto.setId(location.getId());
            result.add(dto);
        }
        return result;
    }

    @Transactional
    public void saveLocation(LocationRequestDto locationRequestDto) {
        User user = UserContextHolder.get();
        Location location = Location.builder()
                .name(locationRequestDto.getCityName())
                .userId(user)
                .latitude(locationRequestDto.getLatitude())
                .longitude(locationRequestDto.getLongitude())
                .build();
            locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(LocationRequestDto locationRequestDto) {
        User user = UserContextHolder.get();
        locationRepository.deleteLocationByIdAndUser(user, locationRequestDto.getId());
    }

    public boolean existsLocation(LocationRequestDto locationRequestDto) {
        User user = UserContextHolder.get();
        boolean exists = locationRepository.getAllUserLocations(user).stream()
                .anyMatch(l ->
                        l.getName().equalsIgnoreCase(locationRequestDto.getCityName())
                        && l.getLatitude().compareTo(locationRequestDto.getLatitude()) == 0
                        && l.getLongitude().compareTo(locationRequestDto.getLongitude()) == 0
                );
        return exists;
    }
}
