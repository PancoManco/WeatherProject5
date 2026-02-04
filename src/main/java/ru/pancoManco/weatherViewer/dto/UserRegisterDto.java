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
    @Size(min = 3, max = 10, message = "Имя пользователя должно быть от 3 до 10 символов.")
    private String username;

    @NotBlank
    @Size(min = 3, max=20, message = "Пароль длиной от 3 символов")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Za-z]).*$",
            message = "Пароль должен содержать хотя бы одну цифру и одну букву.")
    private String password;

    @NotBlank
    @Size(min = 3, max=20, message = "Пароль длиной от 3 символов")
    private String  repeatPassword;
}
