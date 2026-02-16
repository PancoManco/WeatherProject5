package ru.pancoManco.weatherViewer.controllerAdvice;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

     private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException ex, Model model) {
        log.error("Ошибка валидации данных от внешнего API: {}", ex.getMessage(), ex);
        model.addAttribute("errorDescription", "Получены некорректные данные от погодного сервиса.");
        return "error";
    }



}
