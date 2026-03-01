package com.omar.spring_native_resilience.driver.controller;

import com.omar.spring_native_resilience.driver.domain.Driver;
import com.omar.spring_native_resilience.driver.service.DriverAssignmentService;
import com.omar.spring_native_resilience.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 🚗 REST API controller for driver assignment operations.
 *
 * <p>
 * Exposes endpoints to assign available drivers to orders.
 * Integrates with {@link DriverAssignmentService} and demonstrates
 * Spring Boot 4 resilience with automatic retries.
 */
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    private final DriverAssignmentService driverService;

    public DriverController(DriverAssignmentService driverService) {
        this.driverService = driverService;
    }

    /**
     * Assigns a driver to the given order.
     *
     * <p>
     * This endpoint creates a sample order for demonstration purposes.
     * Driver assignment uses RetryTemplate, so logs will show retry attempts
     * if no drivers are immediately available.
     *
     * @param orderId the ID of the order to assign a driver for
     * @return a JSON response containing driver details or error message
     */
    @PostMapping("/assign")
    public ResponseEntity<Map<String, Object>> assignDriver(@RequestParam String orderId) {
        log.info("🚗 API request: Assign driver for order {}", orderId);

        try {
            // Create a sample order
            Order order = new Order(
                    orderId,
                    "customer-123",
                    "rest-001",
                    List.of("item-1", "item-2"),
                    new BigDecimal("25.99"),
                    "payment-123"
            );

            // This call uses RetryTemplate - watch the logs for detailed retry events!
            Driver driver = driverService.assignDriver(order);

            return ResponseEntity.ok(Map.of(
                    "orderId", orderId,
                    "driver", Map.of(
                            "id", driver.id(),
                            "name", driver.name(),
                            "rating", driver.rating()
                    ),
                    "message", "Driver assigned successfully (possibly after retries)"
            ));

        } catch (Exception e) {
            log.error("❌ Failed to assign driver after all retries: {}", e.getMessage());
            return ResponseEntity.status(503).body(Map.of(
                    "error", "No drivers available",
                    "message", e.getMessage(),
                    "orderId", orderId
            ));
        }
    }


}