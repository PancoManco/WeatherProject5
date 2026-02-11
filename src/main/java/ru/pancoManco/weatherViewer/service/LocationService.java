package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pancoManco.weatherViewer.dto.LocationRequestDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;
    private final SessionService sessionService;

    public List<OpenWeatherCityResponseDto> getAllLocationForUser() {


        List<OpenWeatherCityResponseDto> newlocations = new ArrayList<>();

        return newlocations;
    }

    public void saveLocation(LocationRequestDto locationRequestDto) {


    }

    public void deleteLocation(LocationRequestDto locationRequestDto) {
    }
}
