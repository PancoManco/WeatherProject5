package ru.pancoManco.weatherViewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
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


        private BigDecimal longitude;
        private BigDecimal latitude;

        private String description;
        private String icon;

        private double temperature;
        private double fellsLikeTemperature;

        private int humidity;

        private String country;

        private String city;

    @JsonProperty("coord")
    private void unpackCoordinates(JsonNode node) {
        try {
            Optional.ofNullable(node.get("lon"))
                    .ifPresent(n -> longitude = BigDecimal.valueOf(n.asDouble()));
            Optional.ofNullable(node.get("lat"))
                    .ifPresent(n -> latitude = BigDecimal.valueOf(n.asDouble()));
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
                .ifPresent(n -> fellsLikeTemperature = n.asDouble());
        Optional.ofNullable(node.get("humidity"))
                .ifPresent(n -> humidity = n.asInt());
    }

    @JsonProperty("sys")
    private void unpackSys(JsonNode node) {
        Optional.ofNullable(node.get("country"))
                .ifPresent(n -> country = n.asText());
    }


}
