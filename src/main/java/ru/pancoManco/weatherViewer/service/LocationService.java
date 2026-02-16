package ru.pancoManco.weatherViewer.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.context.UserContextHolder;
import ru.pancoManco.weatherViewer.dto.LocationRequestDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.model.AuthUser;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;
    private final UserService userService;
    private final Validator validator;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    public List<OpenWeatherCityResponseDto> getAllLocationForUser() {
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
        List<Location> locations = locationRepository.getAllUserLocations(user);
        List<OpenWeatherCityResponseDto> result = new ArrayList<>();
        for (Location location : locations) {
            OpenWeatherCityResponseDto dto =
                    openWeatherService.getWeatherByCoordinates(
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getName()
                    );
            validateDto(dto,location);

            dto.setId(location.getId());
            OpenWeatherCityResponseDto.Coord originalCoord = new OpenWeatherCityResponseDto.Coord();
            originalCoord.setLat(location.getLatitude());
            originalCoord.setLon(location.getLongitude());
            dto.setCoord(originalCoord);
            result.add(dto);
        }
        return result;
    }

    @Transactional
    public void saveLocation(LocationRequestDto locationRequestDto) {
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
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
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
        locationRepository.deleteLocationByIdAndUser(user, locationRequestDto.getId());
    }

    public boolean existsLocation(LocationRequestDto locationRequestDto) {
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
        boolean exists = locationRepository.getAllUserLocations(user).stream()
                .anyMatch(l ->
                        l.getName().equalsIgnoreCase(locationRequestDto.getCityName())
                        && l.getLatitude().compareTo(locationRequestDto.getLatitude()) == 0
                        && l.getLongitude().compareTo(locationRequestDto.getLongitude()) == 0
                );
        return exists;
    }

    private void validateDto(OpenWeatherCityResponseDto dto,Location location) {
        Set<ConstraintViolation<OpenWeatherCityResponseDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<OpenWeatherCityResponseDto> violation : violations) {
                log.warn("Валидация не пройдена для города {}. Поле: {}, Ошибка: {}",
                        location.getName(),
                        violation.getPropertyPath(),
                        violation.getMessage());
            }
            //  throw new ConstraintViolationException("Данные от API некорректны", violations);
        }
    }
}
