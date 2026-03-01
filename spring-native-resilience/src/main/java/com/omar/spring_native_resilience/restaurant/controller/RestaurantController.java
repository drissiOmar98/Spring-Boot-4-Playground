package com.omar.spring_native_resilience.restaurant.controller;

import com.omar.spring_native_resilience.order.Order;
import com.omar.spring_native_resilience.restaurant.domain.MenuItem;
import com.omar.spring_native_resilience.restaurant.domain.Restaurant;
import com.omar.spring_native_resilience.restaurant.service.RestaurantNotificationService;
import com.omar.spring_native_resilience.restaurant.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 🍴 REST controller for restaurant-related operations.
 *
 * <p>
 * Provides endpoints to:
 * <ul>
 *     <li>List all restaurants</li>
 *     <li>Fetch restaurant menus (with retryable resilient calls)</li>
 *     <li>Simulate lunch rush to demonstrate concurrency limits (@ConcurrencyLimit)</li>
 *     <li>Support virtual thread simulations (Java 21+)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;
    private final RestaurantNotificationService restaurantNotificationService;

    public RestaurantController(RestaurantService restaurantService, RestaurantNotificationService restaurantNotificationService) {
        this.restaurantService = restaurantService;
        this.restaurantNotificationService = restaurantNotificationService;
    }

    @GetMapping("/")
    public List<Restaurant> findAllRestaurants() {
        return restaurantService.findAll();
    }


}