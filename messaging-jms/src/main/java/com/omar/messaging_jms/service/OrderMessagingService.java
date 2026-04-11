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


}