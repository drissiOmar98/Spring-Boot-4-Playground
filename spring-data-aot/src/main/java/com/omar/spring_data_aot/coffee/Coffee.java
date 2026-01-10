package com.omar.spring_data_aot.coffee;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("coffee")
public record Coffee(
        @Id Long id,
        String name,
        String description,
        BigDecimal price,
        Size size
) {
    public Coffee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Coffee name cannot be blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Coffee price must be greater than zero");
        }
        if (size == null) {
            throw new IllegalArgumentException("Coffee size cannot be null");
        }
    }

    public Coffee(String name, String description, BigDecimal price, Size size) {
        this(null, name, description, price, size);
    }
}