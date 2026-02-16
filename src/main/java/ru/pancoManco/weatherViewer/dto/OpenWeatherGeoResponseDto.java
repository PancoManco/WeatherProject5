package ru.pancoManco.weatherViewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherGeoResponseDto {

    @NotNull
    @JsonProperty("name")
    private String cityName;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90", message = "Latitude must be >= -90")
    @DecimalMax(value = "90", message = "Latitude must be <=90")
    @JsonProperty("lat")
    private BigDecimal lat;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180", message = "Latitude must be >= -180")
    @DecimalMax(value = "180", message = "Latitude must be <=180")
    @JsonProperty("lon")
    private BigDecimal lon;

    @NotBlank
    @JsonProperty("country")
    private String country;

    @Nullable
    @JsonProperty("state")
    private String state;
}
