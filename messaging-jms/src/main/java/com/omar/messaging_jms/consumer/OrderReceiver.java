package com.omar.messaging_jms.consumer;

import com.omar.messaging_jms.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * OrderReceiver is a JMS message listener responsible for consuming Order messages
 * from the "order-queue".
 *
 * <p>This component demonstrates the use of Spring's @JmsListener annotation
 * to automatically subscribe to a JMS destination and process incoming messages
 * in real-time.</p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *     <li>Listens to the "order-queue" destination</li>
 *     <li>Automatically converts incoming JMS messages into Order objects</li>
 *     <li>Processes messages asynchronously using Spring JMS listener container</li>
 * </ul>
 *
 * <h2>Key Concepts:</h2>
 * <ul>
 *     <li>Event-driven message consumption</li>
 *     <li>JMS Listener model with Spring Boot</li>
 *     <li>Automatic message conversion via MessageConverter</li>
 * </ul>
 *
 * <p>This class is part of the JMS consumer layer in the messaging architecture.</p>
 */
@Component
public class OrderReceiver {

    private static final Logger log = LoggerFactory.getLogger(OrderReceiver.class);
    private static final String ORDER_QUEUE = "order-queue";

    @JmsListener(destination = ORDER_QUEUE)
    public void receiveOrder(Order order) {
        log.info("Received order: {}", order);
    }

}