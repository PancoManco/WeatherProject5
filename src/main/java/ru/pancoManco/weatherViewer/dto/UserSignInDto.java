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
    @Size(min = 3, max = 20, message = "Login must be between 3 and 10 characters.")
    private String username;

    @NotBlank
    @Size(min = 3, max = 20, message = "Password must be at least 3 characters long.")
    private String password;
}
