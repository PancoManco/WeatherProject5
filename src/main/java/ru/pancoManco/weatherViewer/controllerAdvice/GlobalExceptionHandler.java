package ru.pancoManco.weatherViewer.controllerAdvice;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.pancoManco.weatherViewer.exception.ApiConnectionException;
import ru.pancoManco.weatherViewer.exception.ExternalServiceException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ExternalServiceException.class)
    public String handleExternalServiceException(ExternalServiceException ex, Model model) {
        int status = ex.getStatusCode();
        log.error("External service error: {} (status {})", ex.getMessage(), status);

        switch (status) {
            case 400:
                model.addAttribute("errorTitle", "400 - Bad Request");
                model.addAttribute("errorDescription", "Invalid request sent to weather service.");
                break;
            case 404:
                model.addAttribute("errorTitle", "404 - Not Found");
                model.addAttribute("errorDescription", "Requested weather data not found.");
                break;
            case 500:
            default:
                model.addAttribute("errorTitle", "500 - Weather Service Error");
                model.addAttribute("errorDescription", "Weather service is temporarily unavailable. Please try again later.");
                break;
        }
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleJsonDeserialization(RuntimeException e) {
        log.error("JSON parsing failed or unexpected runtime error: {}", e.getMessage(), e);
    }

    @ExceptionHandler(NoResultException.class)
    public void handleNoResultException(NoResultException ex) {
        log.error("NoResultException: {}", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public String handleNotFound(NoHandlerFoundException ex, Model model) {
//        log.warn("Page not found: {}", ex.getRequestURL());
//        model.addAttribute("errorTitle", "404 - Page Not Found");
//        model.addAttribute("errorDescription", "The page you are looking for does not exist.");
//        return "error";
//    }
    @ExceptionHandler(ApiConnectionException.class)
    public String handleToConnectApi(ApiConnectionException ex, Model model) {
        log.error("Failed to connect to Weather API: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "500 - Weather Service Error");
        model.addAttribute("errorDescription", "Weather service is temporarily unavailable. Please try again later.");
        return "error";
    }
}




