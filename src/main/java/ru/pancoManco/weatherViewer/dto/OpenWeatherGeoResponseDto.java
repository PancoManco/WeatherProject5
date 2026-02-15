package ru.pancoManco.weatherViewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherGeoResponseDto {
    @JsonProperty("name")
    private String cityName;
    @JsonProperty("lat")
    private BigDecimal lat;
    @JsonProperty("lon")
    private BigDecimal lon;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
}
