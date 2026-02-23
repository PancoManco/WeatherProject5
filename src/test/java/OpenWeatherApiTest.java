import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;
import ru.pancoManco.weatherViewer.exception.ApiConnectionException;
import ru.pancoManco.weatherViewer.exception.ExternalServiceException;
import ru.pancoManco.weatherViewer.service.OpenWeatherService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherApiTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private OpenWeatherService openWeatherService;

    private static final String API_KEY = "test-api-key";
    private static final String CITY_NAME = "London";
    private static final BigDecimal LAT = BigDecimal.valueOf(51.5074);
    private static final BigDecimal LON = BigDecimal.valueOf(-0.1278);

    @BeforeEach
    void setUp() {
        openWeatherService.setApiKey(API_KEY);
    }

    @Nested
    @DisplayName("Tests for getGeoByCityName method")
    class GetGeoByCityNameTests {

        @Test
        @DisplayName("Should successfully get geo data by city name")
        void getGeoByCityName_success() throws JsonProcessingException {

            String jsonResponse = "[{\"name\":\"London\",\"lat\":51.5074,\"lon\":-0.1278,\"country\":\"GB\"}]";
            OpenWeatherGeoResponseDto geoDto = createValidGeoDto("London", LAT, LON, "GB");

            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherGeoResponseDto[].class)))
                    .thenReturn(new OpenWeatherGeoResponseDto[]{geoDto});
            when(validator.validate(any(OpenWeatherGeoResponseDto.class)))
                    .thenReturn(Collections.emptySet());
            List<OpenWeatherGeoResponseDto> result = openWeatherService.getGeoByCityName(CITY_NAME);
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCityName()).isEqualTo("London");
            verify(restTemplate).getForObject(contains("q=" + CITY_NAME), eq(String.class));
            verify(objectMapper).readValue(eq(jsonResponse), eq(OpenWeatherGeoResponseDto[].class));
            verify(validator).validate(geoDto);
        }

        @Test
        @DisplayName("Should return empty list when API response is empty")
        void getGeoByCityName_emptyResponse() {
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn("");
            List<OpenWeatherGeoResponseDto> result = openWeatherService.getGeoByCityName(CITY_NAME);
            assertThat(result).isEmpty();
            verify(validator, never()).validate(any());
        }

        @Test
        @DisplayName("Should return empty list when API response is null")
        void getGeoByCityName_nullResponse() {
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(null);
            List<OpenWeatherGeoResponseDto> result = openWeatherService.getGeoByCityName(CITY_NAME);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should throw RuntimeException on JSON parsing error")
        void getGeoByCityName_jsonProcessingException() throws JsonProcessingException {
            String jsonResponse = "invalid json";
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherGeoResponseDto[].class)))
                    .thenThrow(new JsonProcessingException("Invalid JSON") {});
            assertThatThrownBy(() -> openWeatherService.getGeoByCityName(CITY_NAME))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Failed to parse geo data from provider")
                    .hasCauseInstanceOf(JsonProcessingException.class);
        }

        @Test
        @DisplayName("Should throw ExternalServiceException on HTTP error")
        void getGeoByCityName_restClientResponseException() {
            RestClientResponseException exception = new RestClientResponseException(
                    "Not Found",
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    null, null, null);
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenThrow(exception);
            assertThatThrownBy(() -> openWeatherService.getGeoByCityName(CITY_NAME))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Weather provider returned an error");
        }

        @Test
        @DisplayName("Should throw ApiConnectionException on connection error")
        void getGeoByCityName_resourceAccessException() {
            ResourceAccessException exception = new ResourceAccessException("Connection timeout");
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenThrow(exception);
            assertThatThrownBy(() -> openWeatherService.getGeoByCityName(CITY_NAME))
                    .isInstanceOf(ApiConnectionException.class)
                    .hasMessage("Failed to connect to OpenWeather API");
        }

        @Test
        @DisplayName("Should filter out invalid geo responses using validator")
        void getGeoByCityName_filtersInvalidResponses() throws JsonProcessingException {
            String jsonResponse = "[{},{}]";
            OpenWeatherGeoResponseDto validDto = createValidGeoDto("Valid", LAT, LON, "GB");
            OpenWeatherGeoResponseDto invalidDto = createValidGeoDto("Invalid", null, null, null);
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherGeoResponseDto[].class)))
                    .thenReturn(new OpenWeatherGeoResponseDto[]{validDto, invalidDto});
            when(validator.validate(eq(validDto))).thenReturn(Collections.emptySet());
            when(validator.validate(eq(invalidDto)))
                    .thenReturn(Set.of(mock(ConstraintViolation.class)));
            List<OpenWeatherGeoResponseDto> result = openWeatherService.getGeoByCityName(CITY_NAME);
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCityName()).isEqualTo("Valid");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if API key is missing")
        void getGeoByCityName_missingApiKey() {
            openWeatherService.setApiKey(null);
            assertThatThrownBy(() -> openWeatherService.getGeoByCityName(CITY_NAME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("API Key is not set");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if API key is blank")
        void getGeoByCityName_blankApiKey() {
            openWeatherService.setApiKey("   ");
            assertThatThrownBy(() -> openWeatherService.getGeoByCityName(CITY_NAME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("API Key is not set");
        }
    }

    @Nested
    @DisplayName("Tests for getWeatherByCoordinates method")
    class GetWeatherByCoordinatesTests {
        @Test
        @DisplayName("Should successfully get weather by coordinates")
        void getWeatherByCoordinates_success() throws JsonProcessingException {
            String jsonResponse = "{\"weather\":[{\"main\":\"Clear\"}],\"main\":{\"temp\":15.5}}";
            OpenWeatherCityResponseDto weatherDto = new OpenWeatherCityResponseDto();
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherCityResponseDto.class)))
                    .thenReturn(weatherDto);
            OpenWeatherCityResponseDto result =
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME);
            assertThat(result).isNotNull();
            assertThat(result.getCity()).isEqualTo(CITY_NAME);
            verify(restTemplate).getForObject(contains("lat=" + LAT), eq(String.class));
            verify(objectMapper).readValue(eq(jsonResponse), eq(OpenWeatherCityResponseDto.class));
        }

        @Test
        @DisplayName("Should throw ExternalServiceException on empty API response")
        void getWeatherByCoordinates_emptyResponse() {
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn("");
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Weather provider returned empty response");
        }

        @Test
        @DisplayName("Should throw ExternalServiceException on null API response")
        void getWeatherByCoordinates_nullResponse() {
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(null);
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(ExternalServiceException.class);
        }

        @Test
        @DisplayName("Should throw RuntimeException on JSON parsing error")
        void getWeatherByCoordinates_jsonProcessingException() throws JsonProcessingException {
            String jsonResponse = "invalid json";
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherCityResponseDto.class)))
                    .thenThrow(new JsonProcessingException("Invalid JSON") {});
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Failed to parse weather data from provider");
        }

        @Test
        @DisplayName("Should throw ExternalServiceException on HTTP error")
        void getWeatherByCoordinates_restClientResponseException() {
            RestClientResponseException exception = new RestClientResponseException(
                    "Error",
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    null, null, null);
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenThrow(exception);
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Weather provider returned an error");
        }

        @Test
        @DisplayName("Should throw ApiConnectionException on connection error")
        void getWeatherByCoordinates_resourceAccessException() {
            ResourceAccessException exception = new ResourceAccessException("Timeout");
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenThrow(exception);
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(ApiConnectionException.class)
                    .hasMessage("Failed to connect to OpenWeather API");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException if API key is missing")
        void getWeatherByCoordinates_missingApiKey() {
            openWeatherService.setApiKey(null);
            assertThatThrownBy(() ->
                    openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("API Key is not set");
        }
    }

    @Nested
    @DisplayName("Tests for URL building")
    class UrlBuildingTests {

        @Test
        @DisplayName("Should build correct URL for geo API")
        void getGeoByCityName_buildsCorrectUrl() throws JsonProcessingException {
            String jsonResponse = "[]";
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherGeoResponseDto[].class)))
                    .thenReturn(new OpenWeatherGeoResponseDto[0]);
            openWeatherService.getGeoByCityName(CITY_NAME);
            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplate).getForObject(urlCaptor.capture(), eq(String.class));
            String capturedUrl = urlCaptor.getValue();

            assertThat(capturedUrl).contains("https://api.openweathermap.org/geo/1.0/direct");
            assertThat(capturedUrl).contains("q=" + CITY_NAME);
            assertThat(capturedUrl).contains("limit=5");
            assertThat(capturedUrl).contains("appid=" + API_KEY);
        }

        @Test
        @DisplayName("Should build correct URL for weather API")
        void getWeatherByCoordinates_buildsCorrectUrl() throws JsonProcessingException {
            String jsonResponse = "{}";
            when(restTemplate.getForObject(anyString(), eq(String.class)))
                    .thenReturn(jsonResponse);
            when(objectMapper.readValue(eq(jsonResponse), eq(OpenWeatherCityResponseDto.class)))
                    .thenReturn(new OpenWeatherCityResponseDto());
            openWeatherService.getWeatherByCoordinates(LAT, LON, CITY_NAME);
            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplate).getForObject(urlCaptor.capture(), eq(String.class));
            String capturedUrl = urlCaptor.getValue();

            assertThat(capturedUrl).contains("https://api.openweathermap.org/data/2.5/weather");
            assertThat(capturedUrl).contains("lat=" + LAT);
            assertThat(capturedUrl).contains("lon=" + LON);
            assertThat(capturedUrl).contains("units=metric");
            assertThat(capturedUrl).contains("appid=" + API_KEY);
        }
    }
    private OpenWeatherGeoResponseDto createValidGeoDto(String name, BigDecimal lat,
                                                        BigDecimal lon, String country) {
        OpenWeatherGeoResponseDto dto = new OpenWeatherGeoResponseDto();
        dto.setCityName(name);
        dto.setLat(lat);
        dto.setLon(lon);
        dto.setCountry(country);
        return dto;
    }
}
