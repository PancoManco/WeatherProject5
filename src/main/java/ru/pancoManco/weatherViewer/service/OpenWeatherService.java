package ru.pancoManco.weatherViewer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class OpenWeatherService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.key}")
    private String apiKey;

    public List<OpenWeatherGeoResponseDto> getGeoByCityName(String city) {
        if(apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key is not set");
        }
        String url = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/geo/1.0/direct")
                .queryParam("q",city)
                .queryParam("limit",5)
                .queryParam("appid",apiKey)
                .toUriString();
        String JsonResponse = restTemplate.getForObject(url, String.class);
        try {
            return Arrays.asList(objectMapper.readValue(JsonResponse,OpenWeatherGeoResponseDto[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации ",e);
        }
    }

    public OpenWeatherCityResponseDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon,String city) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat",lat)
                .queryParam("lon",lon)
                .queryParam("appid",apiKey)
                .queryParam("units", "metric")
                .toUriString();
        String JsonResponse = restTemplate.getForObject(url, String.class);
        try {
            OpenWeatherCityResponseDto openWeatherGeoResponseDto = objectMapper.readValue(JsonResponse,OpenWeatherCityResponseDto.class);
            openWeatherGeoResponseDto.setCity(city);
            return openWeatherGeoResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации",e);
        }
    }


}
