package ru.pancoManco.weatherViewer.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequestDto {
    private Long id;
    private String cityName;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
