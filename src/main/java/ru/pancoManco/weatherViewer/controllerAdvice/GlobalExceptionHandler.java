package ru.pancoManco.weatherViewer.controllerAdvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
import ru.pancoManco.weatherViewer.exception.UserAlreadyExistException;
import ru.pancoManco.weatherViewer.exception.WrongCredentialsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistsException(
            UserAlreadyExistException ex, Model model) {
        model.addAttribute("newUser", new UserRegisterDto());
        model.addAttribute("userAlreadyExist", true);
        return "sign-up-with-errors";
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public String handleWrongPasswordException(
            WrongCredentialsException ex, Model model) {
        model.addAttribute("newUser", new UserSignInDto());
        model.addAttribute("InvalidUsernameOrPassword", true);
        return "sign-in-with-errors";
    }
}
