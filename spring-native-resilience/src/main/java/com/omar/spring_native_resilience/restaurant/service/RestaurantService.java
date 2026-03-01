package com.omar.spring_native_resilience.restaurant.service;

import com.omar.spring_native_resilience.restaurant.domain.MenuItem;
import com.omar.spring_native_resilience.restaurant.domain.Restaurant;
import com.omar.spring_native_resilience.restaurant.exception.RestaurantApiException;
import com.omar.spring_native_resilience.restaurant.loader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Random;

/**
 * 🍽️ Service responsible for interacting with partner restaurant systems.
 *
 * <p>
 * This class demonstrates Spring Boot 4's <b>native resilience features</b>:
 * <ul>
 *   <li>🔁 Automatic retries using {@link Retryable}</li>
 *   <li>⏳ Exponential backoff strategy</li>
 *   <li>🎯 Domain-specific retry conditions</li>
 * </ul>
 *
 * <p>
 * The partner API is intentionally simulated as <i>flaky</i> to showcase
 * how retry logic behaves in real-world distributed systems.
 */
@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final DataLoader dataLoader;

    /**
     * Used to simulate random API failures.
     */
    private final Random random = new Random();

    public RestaurantService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Fetches menu items from a partner restaurant API.
     *
     * <p>
     * ⚠️ This method simulates an unreliable external system:
     * <ul>
     *   <li>40% chance of failure</li>
     *   <li>Artificial network latency</li>
     * </ul>
     *
     * <p>
     * 🔁 <b>Retry behavior</b>:
     * <ul>
     *   <li>Max attempts: 4 (1 initial + 3 retries)</li>
     *   <li>Retry condition: {@link RestaurantApiException}</li>
     *   <li>Backoff: starts at 1s, doubles on each retry</li>
     * </ul>
     *
     * <p>
     * This demonstrates how Spring Boot 4 provides resilience
     * <b>without external retry libraries</b>.
     *
     * @param restaurantId the partner restaurant identifier
     * @return list of available menu items
     */
    @Retryable(
            maxRetries = 4,
            includes = RestaurantApiException.class,
            delay = 1000, // 1-second delay
            multiplier = 2 // double the delay for each retry attempt
    )
    public List<MenuItem> getMenuFromPartner(String restaurantId) {
        log.info("🍽️  Fetching menu from restaurant partner API for: {}", restaurantId);

        // ❌ Simulate flaky external API (40% failure rate)
        if (random.nextDouble() < 0.4) {
            log.warn("⚠️ Restaurant API failed! Will retry...");
            throw new RestaurantApiException("Partner restaurant API is temporarily unavailable");
        }

        // 🌐 Simulate network latency
        simulateDelay(Duration.ofMillis(200));

        Restaurant restaurant = dataLoader.getRestaurant(restaurantId);
        if (restaurant == null) {
            throw new RestaurantApiException("Restaurant not found: " + restaurantId);
        }

        // ✅ Resolve menu items and filter only available ones
        List<MenuItem> menu = restaurant.menuItemIds().stream()
                .map(dataLoader::getMenuItem)
                .filter(item -> item != null && item.available())
                .toList();

        log.info("✅ Successfully fetched {} menu items from {}", menu.size(), restaurant.name());
        return menu;
    }

    /**
     * Returns all registered restaurants.
     * Used by APIs that do not require resilience or retries.
     */
    public List<Restaurant> findAll() {
        return dataLoader.getRestaurants().values().stream().toList();
    }

    /**
     * Simulates network delay for external API calls.
     */
    private void simulateDelay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}