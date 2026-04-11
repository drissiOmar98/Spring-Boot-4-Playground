package com.omar.messaging_jms.config;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/**
 * Custom JMS MessageConverter implementation using Jackson 3 (JsonMapper).
 *
 * <p>
 * This converter is responsible for:
 * <ul>
 *     <li>Serializing Java objects into JSON and wrapping them into {@link TextMessage}</li>
 *     <li>Deserializing JSON messages back into Java objects</li>
 * </ul>
 *
 * <p>
 * It adds a custom JMS property <b>_type</b> to store the fully qualified class name,
 * allowing dynamic deserialization on the consumer side.
 *
 * <p>
 * Supports only {@link TextMessage}.
 *
 * <p>
 * This implementation is particularly useful in messaging systems where:
 * <ul>
 *     <li>Multiple message types are exchanged</li>
 *     <li>Loose coupling between producer and consumer is required</li>
 * </ul>
 *
 * @author Omar Drissi
 */
@Component
public class JacksonJmsMessageConverter implements MessageConverter {

    private final JsonMapper jsonMapper;

    /**
     * Initializes the JsonMapper with all available modules
     * (e.g., Java Time module for LocalDateTime support).
     */
    public JacksonJmsMessageConverter() {
        this.jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    /**
     * Converts a Java object into a JMS {@link Message}.
     *
     * @param object  the object to convert
     * @param session the JMS session
     * @return a {@link TextMessage} containing the JSON representation of the object
     * @throws JMSException if serialization fails
     */
    @Override
    public Message toMessage(Object object, Session session) throws JMSException {
        try {
            String json = jsonMapper.writeValueAsString(object);
            TextMessage message = session.createTextMessage(json);
            message.setStringProperty("_type", object.getClass().getName());
            return message;
        } catch (Exception e) {
            throw new JMSException("Failed to convert to JSON: " + e.getMessage());
        }
    }

    /**
     * Converts a JMS {@link Message} back into a Java object.
     *
     * @param message the JMS message
     * @return the deserialized Java object
     * @throws JMSException if deserialization fails or message type is unsupported
     */
    @Override
    public Object fromMessage(Message message) throws JMSException {
        if (message instanceof TextMessage textMessage) {
            try {
                String json = textMessage.getText();
                String className = message.getStringProperty("_type");
                Class<?> clazz = Class.forName(className);
                return jsonMapper.readValue(json, clazz);
            } catch (Exception e) {
                throw new JMSException("Failed to parse JSON: " + e.getMessage());
            }
        }
        throw new JMSException("Only TextMessage is supported");
    }
}