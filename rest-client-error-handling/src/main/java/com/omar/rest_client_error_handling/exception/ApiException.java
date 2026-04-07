package com.omar.rest_client_error_handling.exception;

import org.springframework.http.HttpStatusCode;

public class ApiException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public ApiException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}