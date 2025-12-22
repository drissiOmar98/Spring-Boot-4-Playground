package com.omar.null_safety_jspecify_nullaway.menu;

import org.jspecify.annotations.Nullable;

public class Coffee {

    // ============================================
    // 5. CONSTRUCTOR NULL SAFETY
    // ============================================

    private final String type;        // Non-null
    private final String size;        // Non-null
    @Nullable
    private final String customization;  // Can be null

    // Constructor parameters inherit @NullMarked defaults
    public Coffee(String type, String size, @Nullable String customization) {
        this.type = type;  // No null check needed
        this.size = size;  // No null check needed
        this.customization = customization;  // Can be null
    }

    // Getters show null safety in return types
    public String getType() { return type; }

    @Nullable
    public String getCustomization() { return customization; }

}