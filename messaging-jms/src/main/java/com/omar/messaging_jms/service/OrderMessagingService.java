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

/**
 * OrderMessagingService demonstrates advanced usage of Spring Boot 4 JmsClient.
 *
 * <p>This service showcases multiple messaging patterns using the new fluent JMS API,
 * replacing traditional JmsTemplate with a modern, readable, and type-safe approach.</p>
 *
 * <h2>Features Demonstrated:</h2>
 * <ul>
 *     <li>Basic message sending with fluent API</li>
 *     <li>Quality of Service (QoS) configuration (TTL, priority, delivery delay)</li>
 *     <li>Custom message headers for metadata and routing</li>
 *     <li>Synchronous message receiving with timeout</li>
 *     <li>Type-safe message consumption with automatic conversion</li>
 *     <li>Request-Reply (RPC-style) messaging over JMS</li>
 *     <li>Reusable operation handles for performance optimization</li>
 * </ul>
 *
 * <h2>Key Concepts:</h2>
 * <ul>
 *     <li>Fluent API design introduced in Spring Boot 4 JmsClient</li>
 *     <li>Message conversion using custom Jackson-based MessageConverter</li>
 *     <li>JMS QoS controls for enterprise-grade message delivery</li>
 *     <li>Header-based metadata propagation</li>
 *     <li>Blocking vs non-blocking message consumption patterns</li>
 * </ul>
 *
 * <p>This class is intended for learning and demonstration purposes in a Spring Boot 4
 * JMS messaging context using Apache ActiveMQ Artemis.</p>
 */
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

    /**
     * DEMO 5: Receive and Convert (Type-Safe Retrieval)
     *
     * Demonstrates the streamlined receive() method with automatic type conversion.
     *
     * Key Takeaways:
     * - receive(Class<T>) automatically converts and casts to the specified type
     * - Eliminates manual casting and reduces boilerplate code
     * - Returns Optional<Order> directly - cleaner than Optional<Message<?>>
     * - MessageConverter handles deserialization transparently
     *
     * Use Case: When you only need the payload (not headers) and want type-safe,
     * concise code. Compare to Demo 4 - this is simpler when headers aren't needed.
     */
    public Optional<Order> receiveAndConvertOrder() {
        log.info("Receiving and converting order...");

        return jmsClient
                .destination(ORDER_QUEUE)
                .withReceiveTimeout(3000)
                .receive(Order.class);
    }

    /**
     * DEMO 6: Request-Reply Pattern (Synchronous RPC over JMS)
     *
     * Demonstrates synchronous request-response messaging using sendAndReceive().
     *
     * Key Takeaways:
     * - sendAndReceive() implements Remote Procedure Call (RPC) pattern over JMS
     * - Automatically creates temporary reply queue and manages correlation IDs
     * - Blocks until reply arrives or timeout expires (10000ms = 10 seconds)
     * - Returns Optional<Message<?>> containing the response from the processor
     * <p>
     * Use Case: Scenarios requiring immediate confirmation or processed result, such as
     * order validation, payment processing, or any operation where you need to wait for
     * and act on the response. Note: This blocks the calling thread - use async patterns
     * for high-throughput scenarios.
     */
    public Order processOrderSynchronously(Order order) {
        log.info("Processing order synchronously: {}", order.orderId());

        Message<Order> request = MessageBuilder
                .withPayload(order)
                .setHeader("operation", "process")
                .build();

        Optional<Message<?>> reply = jmsClient
                .destination("order-processor")
                .withReceiveTimeout(10000)
                .sendAndReceive(request);

        return reply
                .map(msg -> (Order) msg.getPayload())
                .orElseThrow(() -> new RuntimeException("No reply received"));
    }

    /**
     * DEMO 7: Reusable Operation Handle (Performance Optimization)
     * <p>
     * Demonstrates creating a pre-configured operation handle for repeated use.
     * <p>
     * Key Takeaways:
     * - JmsClient builder methods return an OperationHandle that can be reused
     * - Configure destination and QoS settings once, then call send() multiple times
     * - Eliminates duplicate configuration code when sending similar messages
     * - Improves performance by avoiding repeated builder chain setup
     * - Handle can be stored as instance variable for use across method calls
     * <p>
     * Use Case: Bulk operations or scenarios where you send multiple messages to the
     * same destination with identical QoS settings. Example: processing a batch of
     * express orders, sending notifications, or any repetitive messaging task.
     */
    public void demonstrateReusableHandle() {
        // Create a reusable handle with preset QoS
        var expressHandle = jmsClient
                .destination("express-orders")
                .withTimeToLive(60000)
                .withPriority(8);

        // Use the handle multiple times
        Order order1 = new Order("EXP-001", "CUST-1", new BigDecimal("99.99"),
                Order.OrderStatus.PENDING, LocalDateTime.now());
        Order order2 = new Order("EXP-002", "CUST-2", new BigDecimal("149.99"),
                Order.OrderStatus.PENDING, LocalDateTime.now());

        expressHandle.send(order1);
        expressHandle.send(order2);

        log.info("Sent multiple express orders using reusable handle");
    }
}