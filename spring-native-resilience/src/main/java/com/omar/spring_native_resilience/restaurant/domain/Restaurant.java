package com.omar.spring_native_resilience.restaurant.domain;

import java.util.List;

/**
 * 🏪 Restaurant
 * <p>
 * Represents a food provider available on the QuickBytes platform.
 * </p>
 *
 * <p>
 * Restaurants act as an aggregate root and reference menu items
 * via their identifiers rather than embedding them directly.
 * This keeps the model:
 * <ul>
 *   <li>Lightweight</li>
 *   <li>Scalable</li>
 *   <li>Service-boundary friendly</li>
 * </ul>
 * </p>
 *
 * @param id           Unique identifier of the restaurant
 * @param name         Public display name
 * @param cuisine      Type of cuisine (e.g. Italian, Mexican, Japanese)
 * @param rating       Customer rating (0.0 – 5.0)
 * @param address      Physical address (used for delivery & discovery)
 * @param menuItemIds  List of menu item IDs offered by this restaurant
 */
public record Restaurant(
        String id,
        String name,
        String cuisine,
        double rating,
        String address,
        List<String> menuItemIds
) {}