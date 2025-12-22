package com.omar.null_safety_jspecify_nullaway.orders;

import org.jspecify.annotations.Nullable;

public record Order(String email, @Nullable String promoCode) {
}