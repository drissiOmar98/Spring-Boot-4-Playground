package com.omar.messaging_jms.controller;

import com.omar.messaging_jms.domain.Order;
import com.omar.messaging_jms.service.OrderMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderMessagingService messagingService;

    public OrderController(OrderMessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/simple")
    public ResponseEntity<String> sendSimple(@RequestBody Order order) {
        messagingService.sendSimpleOrder(order);
        return ResponseEntity.ok("Order sent: " + order.orderId());
    }

    @PostMapping("/priority")
    public ResponseEntity<String> sendPriority(@RequestBody Order order) {
        messagingService.sendPriorityOrder(order);
        return ResponseEntity.ok("Priority order sent with QoS settings: " + order.orderId());
    }

    @PostMapping("/with-metadata")
    public ResponseEntity<String> sendWithMetadata(@RequestBody Order order,
                                                   @RequestParam String region) {
        Map<String, Object> metadata = Map.of("region", region);
        messagingService.sendOrderWithMetadata(order, metadata);
        return ResponseEntity.ok("Order sent with metadata: " + order.orderId());
    }

    @GetMapping("/receive")
    public ResponseEntity<Order> receiveOrder() {
        return messagingService.receiveOrder()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/process")
    public ResponseEntity<Order> processOrder(@RequestBody Order order) {
        Order processed = messagingService.processOrderSynchronously(order);
        return ResponseEntity.ok(processed);
    }

    @PostMapping("/bulk-express")
    public ResponseEntity<String> sendBulkExpress() {
        messagingService.demonstrateReusableHandle();
        return ResponseEntity.ok("Bulk express orders sent");
    }

}