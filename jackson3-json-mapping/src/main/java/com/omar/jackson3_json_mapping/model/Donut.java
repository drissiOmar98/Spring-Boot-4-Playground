package com.omar.jackson3_json_mapping.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.omar.jackson3_json_mapping.Views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 🍩 Donut domain model used to demonstrate advanced JSON serialization
 * features introduced with Jackson 3 in Spring Boot 4.
 *
 * <p>This record showcases:</p>
 * <ul>
 *   <li>👁️ <b>JSON Views</b> for controlling field visibility per API consumer</li>
 *   <li>💰 <b>Custom JSON formatting</b> using {@link JsonFormat}</li>
 *   <li>🧵 <b>Immutable data modeling</b> via Java records</li>
 *   <li>🔐 <b>Internal vs public API separation</b></li>
 * </ul>
 *
 * <p>Different clients can receive different JSON representations
 * of the same object without creating multiple DTOs.</p>
 *
 * @param type      🍩 Donut type (exposed in summary views)
 * @param glaze     🍫 Glaze flavor applied to the donut
 * @param toppings  ✨ Optional list of toppings
 * @param price     💲 Price formatted as a string (currency-style)
 * @param isVegan   🌱 Indicates whether the donut is vegan-friendly
 * @param calories  🔥 Internal nutritional information (not public)
 * @param bakedAt   🕒 Internal timestamp of when the donut was baked
 */
public record Donut(
        // Visible in summary responses (e.g. listings)
        @JsonView(Views.Summary.class)
        String type,

        // Public-facing glaze information
        @JsonView(Views.Public.class)
        Glaze glaze,

        // Public toppings list
        @JsonView(Views.Public.class)
        List<String> toppings,

        // Price formatted as a readable currency string
        @JsonView(Views.Summary.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "$#.##")
        BigDecimal price,

        // Public dietary flag
        @JsonView(Views.Public.class)
        Boolean isVegan,

        // Internal-only nutritional data
        @JsonView(Views.Internal.class)
        Integer calories,

        // Internal production timestamp
        @JsonView(Views.Internal.class)
        LocalDateTime bakedAt
) {

    /**
     * 🍯 Available glaze flavors for donuts.
     *
     * <p>Using an enum ensures type safety and predictable JSON values.</p>
     */
    public enum Glaze {
        CHOCOLATE,
        VANILLA,
        STRAWBERRY,
        MAPLE,
        CINNAMON_SUGAR,
        POWDERED_SUGAR,
        NONE
    }
}