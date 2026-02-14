package ru.pancoManco.weatherViewer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pancoManco.weatherViewer.annotation.PasswordMatch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatch
public class UserRegisterDto {

    @NotBlank
    @Size(min = 3, max = 10,
            message = "Username must be between 3 and 10 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9_]+$",
            message = "Username can contain only letters, digits and underscore."
    )
    private String username;

    @NotBlank
    @Size(min = 3, max = 20,
            message = "Password must be at least 3 characters long.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[A-Za-z]).*$",
            message = "Password must contain at least one letter and one digit."
    )
    private String password;

    @NotBlank
    @Size(min = 3, max=20, message = "Password must be at least 3 characters long.")
    private String  repeatPassword;
}
