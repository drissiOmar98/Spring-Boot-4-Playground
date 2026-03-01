package com.omar.spring_native_resilience.driver.exception;

/**
 * ⚠️ Custom exception thrown when no delivery drivers are available to fulfill an order.
 *
 * <p>
 * This runtime exception allows services to trigger fallback logic,
 * retries, or alert mechanisms when driver resources are exhausted.
 */
public class NoDriversAvailableException extends RuntimeException {
    public NoDriversAvailableException(String message) {
        super(message);
    }
}