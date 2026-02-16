package ru.pancoManco.weatherViewer.dto;

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
