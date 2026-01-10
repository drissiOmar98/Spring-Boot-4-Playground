package com.omar.spring_data_aot.coffee;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/coffee")
public class CoffeeController {

    private final CoffeeRepository coffeeRepository;

    public CoffeeController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @GetMapping
    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    @GetMapping("/search")
    public List<Coffee> searchCoffeesByName(@RequestParam String pattern) {
        return coffeeRepository.findByNameContainingIgnoreCase(pattern);
    }

    @GetMapping("/filter")
    public List<Coffee> getCoffeesBySizeAndPrice(
            @RequestParam Size size,
            @RequestParam BigDecimal minPrice) {
        return coffeeRepository.findBySizeAndPriceGreaterThan(size, minPrice);
    }

    @GetMapping("/affordable")
    public List<Coffee> getAffordableCoffees(
            @RequestParam(defaultValue = "LARGE") Size size,
            @RequestParam(defaultValue = "6.00") BigDecimal maxPrice) {
        return coffeeRepository.findAffordableCoffeesBySize(size.name(), maxPrice);
    }

}