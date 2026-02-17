package ru.pancoManco.weatherViewer.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.context.UserContextHolder;
import ru.pancoManco.weatherViewer.dto.LocationRequestDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;
import ru.pancoManco.weatherViewer.model.AuthUser;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;
    private final UserService userService;
    private final Validator validator;


    public List<OpenWeatherCityResponseDto> getAllLocationForUser() {
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
        List<Location> locations = locationRepository.getAllUserLocations(user);

        List<CompletableFuture<OpenWeatherCityResponseDto>> futures = locations.stream()
                .map(location -> CompletableFuture.supplyAsync(() ->{
                            OpenWeatherCityResponseDto dto =
                                    openWeatherService.getWeatherByCoordinates(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            location.getName()
                                    );
                            if (!isValidatedResponseDto(dto,location))
                            {
                                OpenWeatherCityResponseDto emptyDto = new OpenWeatherCityResponseDto();
                                emptyDto.setCity(location.getName());
                                return emptyDto;
                            }
                            dto.setId(location.getId());
                            OpenWeatherCityResponseDto.Coord originalCoord= new OpenWeatherCityResponseDto.Coord();
                            originalCoord.setLat(location.getLatitude());
                            originalCoord.setLon(location.getLongitude());
                            dto.setCoord(originalCoord);
                            return dto;
                        })).toList();

        return futures.stream().map(CompletableFuture::join).filter(dto->dto!=null).toList();

//        List<OpenWeatherCityResponseDto> result = new ArrayList<>();
//        for (Location location : locations) {
//            OpenWeatherCityResponseDto dto =
//                    openWeatherService.getWeatherByCoordinates(
//                            location.getLatitude(),
//                            location.getLongitude(),
//                            location.getName()
//                    );
//            if (!isValidatedResponseDto(dto, location)) {
//                continue;
//            }
//            dto.setId(location.getId());
//            OpenWeatherCityResponseDto.Coord originalCoord = new OpenWeatherCityResponseDto.Coord();
//            originalCoord.setLat(location.getLatitude());
//            originalCoord.setLon(location.getLongitude());
//            dto.setCoord(originalCoord);
//            result.add(dto);
//        }
//        return result;
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
    public int deleteLocation(LocationRequestDto locationRequestDto) {
        AuthUser authUser = UserContextHolder.get();
        User user = userService.getUserByUsername(authUser.login());
      return  locationRepository.deleteLocationByIdAndUser(user, locationRequestDto.getId());
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

    private boolean isValidatedResponseDto(OpenWeatherCityResponseDto dto, Location location) {
        Set<ConstraintViolation<OpenWeatherCityResponseDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<OpenWeatherCityResponseDto> violation : violations) {
                log.warn("Валидация не пройдена для города {}. Поле: {}, Ошибка: {}",
                        location.getName(),
                        violation.getPropertyPath(),
                        violation.getMessage());
            }
            return false;
        }
        return true;
    }
}
