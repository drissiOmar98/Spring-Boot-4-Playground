package com.omar.null_safety_jspecify_nullaway.orders;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // ============================================
    // METHOD PARAMETERS
    // ============================================

    /**
     * Creates an order for a user.
     *
     * @param email the user's email (non-null by default)
     * @param promoCode an optional promotional code that may be null
     * @return the created Order
     */
    public Order createOrder(String email, @Nullable String promoCode) {
        // No null check needed for email because of @NullMarked
        sendConfirmation(email);

        // Must check promoCode before using since it's @Nullable
        if (promoCode != null) {
            applyDiscount(promoCode);
        }

        return new Order(email,promoCode);
    }

    private void applyDiscount(@Nullable String promoCode) {
        System.out.println("Applying discount for promo code: " + promoCode);
    }

    private void sendConfirmation(String email) {
        System.out.println("Sending confirmation");
    }

}