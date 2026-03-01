package com.omar.spring_native_resilience.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * 🍔 Represents a food delivery order in the QuickBytes system.
 *
 * <p>
 * This immutable record models:
 * <ul>
 *   <li>Customer information (customerId)</li>
 *   <li>Restaurant source (restaurantId)</li>
 *   <li>Order items (list of item IDs)</li>
 *   <li>Payment details (paymentId, paymentConfirmation)</li>
 *   <li>Order lifecycle status via {@link OrderStatus}</li>
 * </ul>
 *
 * <p>
 * Convenience methods allow updating payment confirmation and status while
 * maintaining immutability.
 */
public record Order(
        String id,
        String customerId,
        String restaurantId,
        List<String> items,
        BigDecimal totalAmount,
        String paymentId,
        String paymentConfirmation,
        OrderStatus status
) {
    /**
     * Convenience constructor for creating a new order before payment confirmation.
     *
     * <p>
     * Automatically sets {@link OrderStatus} to {@code PENDING} and
     * leaves {@code paymentConfirmation} as {@code null}.
     *
     * @param id unique order identifier
     * @param customerId customer placing the order
     * @param restaurantId restaurant fulfilling the order
     * @param items list of menu item IDs
     * @param totalAmount total monetary value of the order
     * @param paymentId payment transaction ID
     */
    public Order(String id, String customerId, String restaurantId,
                 List<String> items, BigDecimal totalAmount, String paymentId) {
        this(id, customerId, restaurantId, items, totalAmount, paymentId, null, OrderStatus.PENDING);
    }

    /**
     * Returns a copy of the order with updated payment confirmation.
     *
     * <p>
     * This method preserves immutability.
     *
     * @param paymentConfirmation confirmation string from payment processor
     * @return new {@link Order} instance with updated payment confirmation
     */
    public Order withPaymentConfirmation(String paymentConfirmation) {
        return new Order(id, customerId, restaurantId, items, totalAmount, paymentId, paymentConfirmation, status);
    }

    /**
     * Returns a copy of the order with updated status.
     *
     * <p>
     * Supports updating the order lifecycle (e.g., PREPARING → OUT_FOR_DELIVERY).
     *
     * @param status new {@link OrderStatus}
     * @return new {@link Order} instance with updated status
     */
    public Order withStatus(OrderStatus status) {
        return new Order(id, customerId, restaurantId, items, totalAmount, paymentId, paymentConfirmation, status);
    }

    /**
     * Represents the lifecycle states of an order.
     */
    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }
}