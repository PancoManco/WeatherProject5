package ru.pancoManco.weatherViewer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;
import ru.pancoManco.weatherViewer.exception.ApiConnectionException;
import ru.pancoManco.weatherViewer.exception.ExternalServiceException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenWeatherService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Validator validator;


    @Value("${api.key}")
    @Setter
    private String apiKey;

    public List<OpenWeatherGeoResponseDto> getGeoByCityName(String city)  {
        checkApiKey();
        String url = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/geo/1.0/direct")
                .queryParam("q",city)
                .queryParam("limit",5)
                .queryParam("appid",apiKey)
                .toUriString();
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            if (jsonResponse==null || jsonResponse.isBlank()) {
                log.warn("OpenWeather API returned empty response");
                return List.of();
            }
           OpenWeatherGeoResponseDto[] response = objectMapper.readValue(jsonResponse,OpenWeatherGeoResponseDto[].class);
            return Arrays.stream(response)
                    .filter(this::isValidGeoResponseDto)
                    .toList();
        }
        catch (JsonProcessingException e) {
            log.error("Deserialization error from OpenWeather Geo API. city={}", city, e);
            throw new RuntimeException("Failed to parse geo data from provider",e);

        }
        catch (RestClientResponseException e) {
            int statusCode = e.getRawStatusCode();
            log.error("OpenWeather returned error. city={}, status={}, body={}",
                    city,
                    statusCode,
                    e.getResponseBodyAsString());
            throw new ExternalServiceException("Weather provider returned an error",statusCode);
        }
        catch (ResourceAccessException e) {
            log.error("Failed to access OpenWeather API. city={}", city, e);
            throw new ApiConnectionException("Failed to connect to OpenWeather API");
        }
    }

    public OpenWeatherCityResponseDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon,String city)  {
        checkApiKey();
        String url = UriComponentsBuilder
                .fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat",lat)
                .queryParam("lon",lon)
                .queryParam("appid",apiKey)
                .queryParam("units", "metric")
                .toUriString();

        try {
            String JsonResponse = restTemplate.getForObject(url, String.class);
            if (JsonResponse == null || JsonResponse.isBlank()) {
                log.warn("OpenWeather returned empty body for coordinates: lat={}, lon={}", lat, lon);
                throw new ExternalServiceException("Weather provider returned empty response");
            }
            OpenWeatherCityResponseDto openWeatherGeoResponseDto = objectMapper.readValue(JsonResponse,OpenWeatherCityResponseDto.class);
            openWeatherGeoResponseDto.setCity(city);
            return openWeatherGeoResponseDto;
        }
        catch (JsonProcessingException e) {
            log.error("Error deserializing weather response for city={}, lat={}, lon={}", city, lat, lon, e);
            throw new RuntimeException("Failed to parse weather data from provider");

        } catch (RestClientResponseException e) {
            int statusCode = e.getRawStatusCode();
            log.error("OpenWeather returned HTTP error. city={}, lat={}, lon={}, status={}, body={}",
                    city, lat, lon, statusCode, e.getResponseBodyAsString());

             throw new ExternalServiceException("Weather provider returned an error",statusCode);
        }
        catch (ResourceAccessException e) {
            log.error("Failed to access OpenWeather API by weather coordinates. city={}", city, e);
            throw  new ApiConnectionException("Failed to connect to OpenWeather API");
        }
    }

    private boolean isValidGeoResponseDto(OpenWeatherGeoResponseDto dto) {
        Set<ConstraintViolation<OpenWeatherGeoResponseDto>> violations =
                validator.validate(dto);
        if (!violations.isEmpty()) {
            violations.forEach(v ->
                    log.warn("Geo validation failed. Field={}, message={}",
                            v.getPropertyPath(),
                            v.getMessage())
            );
            return false;
        }
        return true;
    }

    private void checkApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key is not set");
        }
    }

}
