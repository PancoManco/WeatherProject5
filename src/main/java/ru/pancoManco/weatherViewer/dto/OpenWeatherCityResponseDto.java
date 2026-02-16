package ru.pancoManco.weatherViewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherCityResponseDto {

        private Long id;

        @NotNull(message = "Coordinates are required")
        private Coord coord;

        @NotNull(message = "Weather list cannot be null")
        private Weather[] weather;

        @NotNull(message = "Main info is required")
        private Main main;

        @NotNull(message = "Sys info is required")
        private Sys sys;

        @NotBlank(message = "City name is required")
        private String city;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Coord {

            @NotNull(message = "Latitude is required")
            @DecimalMin(value = "-90.0")
            @DecimalMax(value = "90.0")
            private BigDecimal lat;

            @NotNull(message = "Longitude is required")
            @DecimalMin(value = "-180.0")
            @DecimalMax(value = "180.0")
            private BigDecimal lon;

            public String getLatFormatted() {
                return  lat.stripTrailingZeros().toPlainString();
            }

            public String getLonFormatted() {
                return lon.stripTrailingZeros().toPlainString();
            }
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Weather {
            @NotBlank(message="Description cannot be empty or blank")
            private String description;
            @NotBlank(message="Icon cannot be empty or blank")
            private String icon;
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            private double temp;
            private double feels_like;

            @Min(0)
            @Max(100)
            private int humidity;

        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Sys {
            @NotBlank
            private String country;
        }

}
