package ru.pancoManco.weatherViewer.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {
    private final int statusCode;

    public ExternalServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ExternalServiceException(String message) {
        super(message);
        this.statusCode = 500;
    }

}
