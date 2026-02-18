import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;
import ru.pancoManco.weatherViewer.service.OpenWeatherService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherApiTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Validator validator;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private OpenWeatherService openWeatherService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        openWeatherService.setApiKey("0c663a412fbffaec4e996ebab975073b");
    }

    @Test
    @DisplayName("getGeoByCityName возвращает список локаций")
    void testGetGeoByCityNameSuccess() throws Exception {
        String city = "Moscow";

        OpenWeatherGeoResponseDto dto = new OpenWeatherGeoResponseDto();
        dto.setCityName(city);
        String jsonResponse = objectMapper.writeValueAsString(new OpenWeatherGeoResponseDto[]{dto});

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(jsonResponse);

        when(validator.validate(ArgumentMatchers.any(OpenWeatherGeoResponseDto.class)))
                .thenReturn(java.util.Collections.emptySet());

        List<OpenWeatherGeoResponseDto> result = openWeatherService.getGeoByCityName(city);

        assertEquals(1, result.size());
        assertEquals(city, result.get(0).getCityName());
    }
}
