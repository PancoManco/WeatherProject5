package ru.pancoManco.weatherViewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherCityResponseDto {

        private Long id;
//        @NotNull(message = "Coordinates are required")
//        private Coord coord;
//
//        @NotNull(message = "Weather list cannot be null")
//        private Weather[] weather;
//
//        @NotNull(message = "Main info is required")
//        private Main main;
//
//        @NotNull(message = "Sys info is required")
//        private Sys sys;
//
//        @NotBlank(message = "City name is required")
//        private String city;
//
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        @AllArgsConstructor
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        public static class Coord {
//            @NotNull
//            @DecimalMin(value = "-90.0")
//            @DecimalMax(value = "90.0")
//            private BigDecimal lat;
//
//            @NotNull
//            @DecimalMin(value = "-180.0")
//            @DecimalMax(value = "180.0")
//            private BigDecimal lon;
//        }
//
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        @AllArgsConstructor
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        public static class Weather {
//            @NotBlank
//            private String description;
//            private String icon;
//        }
//
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        @AllArgsConstructor
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        public static class Main {
//            private double temp;
//            private double feels_like;
//
//            @Min(0)
//            @Max(100)
//            private int humidity;
//        }
//
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        @AllArgsConstructor
//        @JsonIgnoreProperties(ignoreUnknown = true)
//        public static class Sys {
//            @NotBlank
//            private String country;
//        }


        private BigDecimal longitude;
        private BigDecimal latitude;

        private String description;
        private String icon;

        private double temperature;
        private double feelsLikeTemperature;

        private int humidity;

        private String country;

        private String city;

    @JsonProperty("coord")
    private void unpackCoordinates(JsonNode node) {
        try {
            Optional.ofNullable(node.get("lon"))
                    .ifPresent(n -> longitude = new BigDecimal(n.asText()));
            Optional.ofNullable(node.get("lat"))
                    .ifPresent(n -> latitude = new BigDecimal(n.asText()));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Ошибка при разборе координат", e);
        }
    }

    @JsonProperty("weather")
    private void unpackWeather(JsonNode node) {
        if (node.isArray() && node.size() > 0) {
            JsonNode firstWeatherEntry = node.get(0);
            Optional.ofNullable(firstWeatherEntry.get("description"))
                    .ifPresent(n -> description = n.asText());
            Optional.ofNullable(firstWeatherEntry.get("icon"))
                    .ifPresent(n -> icon = n.asText());
        }
    }
    @JsonProperty("main")
    private void unpackMain(JsonNode node) {
        Optional.ofNullable(node.get("temp"))
                .ifPresent(n -> temperature = n.asDouble());
        Optional.ofNullable(node.get("feels_like"))
                .ifPresent(n -> feelsLikeTemperature = n.asDouble());
        Optional.ofNullable(node.get("humidity"))
                .ifPresent(n -> humidity = n.asInt());
    }

    @JsonProperty("sys")
    private void unpackSys(JsonNode node) {
        Optional.ofNullable(node.get("country"))
                .ifPresent(n -> country = n.asText());
    }


}
