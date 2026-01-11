package com.omar.spring_data_aot.coffee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CoffeeRepositoryTest {

    @Autowired
    CoffeeRepository coffeeRepository;

    @Test
    void findByNameContainingIgnoreCase_shouldFindMatchingCoffees() {
        List<Coffee> results = coffeeRepository.findByNameContainingIgnoreCase("latte");

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(coffee ->
            coffee.name().toLowerCase().contains("latte"));
    }

    @Test
    void findByNameContainingIgnoreCase_shouldBeCaseInsensitive() {
        List<Coffee> lowercase = coffeeRepository.findByNameContainingIgnoreCase("mocha");
        List<Coffee> uppercase = coffeeRepository.findByNameContainingIgnoreCase("MOCHA");

        assertThat(lowercase).isEqualTo(uppercase);
    }

    @Test
    void findBySizeAndPriceGreaterThan_shouldFilterBySizeAndPrice() {
        List<Coffee> results = coffeeRepository.findBySizeAndPriceGreaterThan(
            Size.LARGE, new BigDecimal("5.00"));

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(coffee ->
            coffee.size() == Size.LARGE &&
            coffee.price().compareTo(new BigDecimal("5.00")) > 0);
    }

    @Test
    void findAffordableCoffeesBySize_shouldFindCoffeesUnderMaxPrice() {
        List<Coffee> results = coffeeRepository.findAffordableCoffeesBySize(
            "LARGE", new BigDecimal("5.50"));

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(coffee ->
            coffee.size() == Size.LARGE &&
            coffee.price().compareTo(new BigDecimal("5.50")) <= 0);
    }

    @Test
    void findAffordableCoffeesBySize_shouldBeOrderedByPriceDescending() {
        List<Coffee> results = coffeeRepository.findAffordableCoffeesBySize(
            "LARGE", new BigDecimal("10.00"));

        assertThat(results).hasSizeGreaterThan(1);
        for (int i = 0; i < results.size() - 1; i++) {
            assertThat(results.get(i).price())
                .isGreaterThanOrEqualTo(results.get(i + 1).price());
        }
    }
}