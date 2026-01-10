package com.omar.spring_data_aot.order;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderController(OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/customer/{customerName}")
    public List<Order> getOrdersByCustomer(@PathVariable String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }

    @GetMapping("/recent")
    public List<Order> getRecentOrdersByStatus(
            @RequestParam OrderStatus status,
            @RequestParam LocalDateTime since) {
        return orderRepository.findByStatusAndOrderDateAfter(status, since);
    }

    @GetMapping("/by-coffee")
    public List<Order> getOrdersByCoffee(@RequestParam String coffeeName) {
        return orderRepository.findOrdersByCoffeeName(coffeeName);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderItemRepository.findOrderItemsWithCoffeeDetails(orderId);
    }
}