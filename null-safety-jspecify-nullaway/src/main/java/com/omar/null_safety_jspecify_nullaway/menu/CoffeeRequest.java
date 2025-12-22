package com.omar.null_safety_jspecify_nullaway.menu;

import org.jspecify.annotations.Nullable;

record CoffeeRequest(
        String email,           // non-null
        String coffeeType,      // non-null
        String size,            // non-null
        @Nullable String milk,  // nullable
        @Nullable String syrup  // nullable
) {
    // Compact constructor for validation
    public CoffeeRequest {
        // These would cause compile errors with NullAway if null passed
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email required");
        }
        if (coffeeType.isEmpty()) {
            throw new IllegalArgumentException("Coffee type required");
        }
    }
}