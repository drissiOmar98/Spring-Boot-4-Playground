package com.omar.messaging_jms.service;

import com.omar.messaging_jms.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsClient;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderMessagingService {

    private static final Logger log = LoggerFactory.getLogger(OrderMessagingService.class);
    private final JmsClient jmsClient;

    private static final String ORDER_QUEUE = "order-queue";

    public OrderMessagingService(JmsClient jmsClient) {
        this.jmsClient = jmsClient;
    }

    /**
     * DEMO 1: Basic Send with Fluent API
     *
     * Demonstrates the simplest way to send a message using JmsClient's fluent API.
     *
     * Key Takeaways:
     * - JmsClient provides a clean, fluent API that replaces the verbose JmsTemplate approach
     * - Only requires destination name and payload - no boilerplate code needed
     * - Automatically handles message conversion using configured MessageConverter
     *
     * Use Case: Simple fire-and-forget messaging where default QoS settings are acceptable
     */
    public void sendSimpleOrder(Order order) {
        log.info("Sending order with basic fluent API: {}", order.orderId());

        jmsClient
                .destination(ORDER_QUEUE)
                .send(order);
    }

    /**
     * DEMO 2: Send with Quality of Service (QoS) Settings
     *
     * Demonstrates how to configure message delivery characteristics using QoS parameters.
     *
     * Key Takeaways:
     * - withTimeToLive(): Sets message expiration (300000ms = 5 minutes)
     * - withPriority(): Controls message processing order (0-9, where 9 is highest)
     * - withDeliveryDelay(): Delays message availability to consumers (1000ms = 1 second)
     * - All QoS settings are optional and can be combined as needed
     *
     * Use Case: High-priority orders that need expedited processing, with automatic expiration
     * to prevent stale order processing
     */
    public void sendPriorityOrder(Order order) {
        log.info("Sending priority order with QoS settings: {}", order.orderId());

        jmsClient
                .destination(ORDER_QUEUE)
                .withTimeToLive(300000)  // 5 minutes TTL
                .withPriority(9)         // Highest priority
                .withDeliveryDelay(1000) // 1-second delay
                .send(order);
    }

    /**
     * DEMO 3: Send with Custom Headers
     *
     * Demonstrates how to attach metadata to messages using Spring's MessageBuilder.
     *
     * Key Takeaways:
     * - Use MessageBuilder to create a Message wrapper with custom headers
     * - Headers enable routing decisions, filtering, and tracking without parsing the payload
     * - JmsClient accepts both plain objects (auto-wrapped) and Message objects
     * - Headers are accessible to consumers without deserializing the message body
     *
     * Use Case: Adding tracking information (source system, region, processing hints) that
     * consumers can use for routing, logging, or conditional processing
     */
    public void sendOrderWithMetadata(Order order, Map<String, Object> metadata) {
        log.info("Sending order with metadata headers: {}", order.orderId());

        Message<Order> message = MessageBuilder
                .withPayload(order)
                .setHeader("source", "web-portal")
                .setHeader("region", metadata.get("region"))
                .setHeader("processedBy", "jms-client-demo")
                .build();

        jmsClient
                .destination(ORDER_QUEUE)
                .withTimeToLive(60000)
                .send(message);
    }

    /**
     * DEMO 4: Synchronous Receive with Timeout
     *
     * Demonstrates blocking message retrieval with configurable timeout.
     *
     * Key Takeaways:
     * - receive() blocks until a message arrives or timeout expires
     * - withReceiveTimeout() prevents indefinite blocking (5000ms = 5 seconds)
     * - Returns Optional<Message<?>> - empty if no message available within timeout
     * - Provides access to both payload and headers via Message object
     *
     * Use Case: Polling for messages in batch processing scenarios where you want to
     * wait for a message but not block forever. The Optional pattern handles the
     * "no message available" case gracefully.
     */
    public Optional<Order> receiveOrder() {
        log.info("Receiving order with timeout...");

        Optional<Message<?>> message = jmsClient
                .destination(ORDER_QUEUE)
                .withReceiveTimeout(5000)  // 5 second timeout
                .receive();

        return message.map(msg -> {
            Order order = (Order) msg.getPayload();
            log.info("Received order: {}", order.orderId());
            return order;
        });
    }


}