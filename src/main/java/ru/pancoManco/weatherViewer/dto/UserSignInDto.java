package ru.pancoManco.weatherViewer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInDto {

    @NotBlank
    @Size(min = 3, max = 20, message = "Логин от 3 до 20 символов")
    private String username;

    @NotBlank
    @Size(min = 3, max = 20, message = "Пароль от 3 до 20 символов")
    private String password;
}
