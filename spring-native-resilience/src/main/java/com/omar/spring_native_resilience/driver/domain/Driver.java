package com.omar.spring_native_resilience.driver.domain;

/**
 * 🚚 Represents a delivery driver in the QuickBytes system.
 *
 * <p>
 * This immutable record holds basic driver information:
 * <ul>
 *   <li>Unique identifier (id)</li>
 *   <li>Driver's full name (name)</li>
 *   <li>Driver rating (rating), e.g., for customer feedback</li>
 * </ul>
 */
public record Driver(
        String id,
        String name,
        double rating
) {}