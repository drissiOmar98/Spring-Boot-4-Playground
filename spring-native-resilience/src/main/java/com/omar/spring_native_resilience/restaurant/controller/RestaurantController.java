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

    /**
     * Get the menu for a specific restaurant.
     * Uses {@link RestaurantService#getMenuFromPartner} which may retry automatically on failure.
     */
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<Map<String, Object>> getRestaurantMenu(@PathVariable String restaurantId) {
        log.info("🍽️  API request: Get menu for restaurant {}", restaurantId);

        try {
            List<MenuItem> menu = restaurantService.getMenuFromPartner(restaurantId);

            return ResponseEntity.ok(Map.of(
                    "restaurantId", restaurantId,
                    "menuItems", menu,
                    "count", menu.size(),
                    "message", "Menu fetched successfully (possibly after retries)"
            ));

        } catch (Exception e) {
            log.error("❌ Failed to fetch menu after all retries: {}", e.getMessage());
            return ResponseEntity.status(503).body(Map.of(
                    "error", "Service temporarily unavailable",
                    "message", e.getMessage(),
                    "restaurantId", restaurantId
            ));
        }
    }

    @GetMapping("/lunch-rush")
    public ResponseEntity<Map<String, Object>> lunchRush() {
        log.info("🍔 LUNCH RUSH STARTED - Simulating 10 concurrent order notifications");
        log.info("⚠️  Concurrency Limit: 3 (only 3 notifications can process simultaneously)");

        LocalDateTime startTime = LocalDateTime.now();

        // Create 10 orders to simulate lunch rush
        List<Order> orders = createSampleOrders("lunch-%04d", 10);


        // Use fixed thread pool with 10 threads to submit all orders concurrently
        ExecutorService executor = Executors.newFixedThreadPool(10);

        log.info("📤 Submitting all 10 orders to thread pool...");

        // Submit all notification tasks concurrently
        for (Order order : orders) {
            executor.submit(() -> safeNotify(order));
        }

        // Shutdown executor and wait for all tasks to complete
        shutdownExecutor(executor);


        LocalDateTime endTime = LocalDateTime.now();
        long durationSeconds = Duration.between(startTime, endTime).toSeconds();

        log.info("\uD83C\uDF89 LUNCH RUSH COMPLETED - All 10 notifications processed in {} seconds", durationSeconds);
        log.info("📊 Expected time: ~6-8 seconds (10 orders / 3 concurrent * 2s each)");

        return ResponseEntity.ok(Map.of(
            "message", "Lunch rush simulation completed",
            "totalOrders", 10,
            "concurrencyLimit", 3,
            "durationSeconds", durationSeconds,
            "expectedDuration", "6-8 seconds",
            "threadPoolType", "Fixed thread pool (10 threads)",
            "explanation", "With @ConcurrencyLimit(3), only 3 notifications process simultaneously. " +
                          "The remaining 7 orders queue and wait for permits to become available."
        ));
    }



    /* ----------------- Helper Methods ----------------- */

    /**
     * Creates a list of sample orders for testing or simulation purposes.
     * Each order is pre-populated with customer, restaurant, items, payment, and status.
     *
     * @param idFormat The string format for generating order IDs (e.g., "lunch-%04d")
     * @param count    The number of orders to create
     * @return A list of sample {@link Order} objects
     */
    private List<Order> createSampleOrders(String idFormat, int count) {
        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            orders.add(new Order(
                    String.format(idFormat, i),
                    "customer-" + i,
                    "restaurant-001",
                    List.of("burger", "fries", "drink"),
                    new BigDecimal("15.99"),
                    "payment-" + i,
                    "confirmed-" + i,
                    Order.OrderStatus.CONFIRMED
            ));
        }
        return orders;
    }

    /**
     * Safely notifies a restaurant of a new order.
     * Exceptions are caught and logged to prevent thread or executor interruption.
     *
     * @param order The {@link Order} to notify the restaurant about
     */
    private void safeNotify(Order order) {
        try {
            restaurantNotificationService.notifyRestaurant(order);
        } catch (Exception e) {
            log.error("❌ Error notifying restaurant for order {}: {}", order.id(), e.getMessage());
        }
    }

    /**
     * Shuts down an {@link ExecutorService} gracefully.
     * Waits up to 2 minutes for tasks to complete, otherwise forces shutdown.
     * Properly handles {@link InterruptedException} and interrupts the current thread.
     *
     * @param executor The {@link ExecutorService} to shut down
     */
    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(2, TimeUnit.MINUTES);
            if (!finished) {
                log.warn("⚠️  Some notifications did not complete within 2 minutes");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("❌ Thread pool interrupted: {}", e.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}