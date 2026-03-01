package com.omar.spring_native_resilience.restaurant;

import java.math.BigDecimal;
/**
 * 🍔 MenuItem
 * <p>
 * Represents a single item that can be ordered from a restaurant
 * in the QuickBytes food delivery domain.
 * </p>
 *
 * <p>
 * This record is immutable by design and well-suited for:
 * <ul>
 *   <li>JSON serialization/deserialization (Jackson 3)</li>
 *   <li>API responses</li>
 *   <li>In-memory demo data or persistence mapping</li>
 * </ul>
 * </p>
 *
 * @param id           Unique identifier of the menu item (stable across services)
 * @param restaurantId Identifier of the restaurant that owns this item
 * @param name         Display name shown to customers
 * @param description Short, user-friendly description of the item
 * @param price        Price of the item (currency-safe using BigDecimal)
 * @param category     Logical category (e.g. Burgers, Drinks, Desserts)
 * @param available    Availability flag (used for filtering and ordering logic)
 */
public record MenuItem(
        String id,
        String restaurantId,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean available
) {}