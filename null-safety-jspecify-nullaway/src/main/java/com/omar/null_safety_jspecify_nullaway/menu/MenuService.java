package com.omar.null_safety_jspecify_nullaway.menu;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    // ============================================
    // 4. ARRAYS WITH NULL SAFETY
    // ============================================

    // Array of non-null strings
    public String[] getMenuCategories() {
        return new String[] {"Coffee", "Tea", "Pastries"};
        // return new String[] {"Coffee", null, "Tea"}; // ❌ Would fail!
    }

    // Array that can contain nulls
    public @Nullable String[] getDailySpecials() {
        return new @Nullable String[] {
                "Pumpkin Spice Latte",
                null,  // Tuesday has no special
                "Blueberry Muffin",
                null,  // Thursday has no special
                "Happy Hour: 50% off pastries"
        };
    }

    // Alternative: Special menu items with possible gaps
    public @Nullable String[] getSeasonalMenuItems() {
        return new @Nullable String[] {
                "Summer Berry Smoothie",
                null,  // Item discontinued
                "Autumn Harvest Salad",
                null,  // Out of stock
                "Winter Spice Chai"
        };
    }

}