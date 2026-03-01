package com.omar.spring_native_resilience.restaurant.exception;

public class RestaurantApiException extends RuntimeException{
    public RestaurantApiException(String message) {
        super(message);
    }
}