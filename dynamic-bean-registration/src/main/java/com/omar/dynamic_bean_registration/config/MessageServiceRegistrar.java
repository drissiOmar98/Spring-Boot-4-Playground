package com.omar.dynamic_bean_registration.config;

import com.omar.dynamic_bean_registration.service.Impl.EmailMessageService;
import com.omar.dynamic_bean_registration.service.Impl.SmsMessageService;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

/**
 * Modern Spring Boot 4 configuration using the {@link BeanRegistrar} interface
 * for dynamic and programmatic bean registration.
 * <p>
 * This registrar replaces the need for low-level {@code BeanDefinitionRegistryPostProcessor}
 * implementations by providing a clean, declarative, and type-safe API for registering beans
 * based on runtime or environment conditions.
 * </p>
 *
 * <p>
 * The concrete {@code MessageService} implementation is selected at startup
 * based on the {@code app.message-type} environment property.
 * </p>
 */
public class MessageServiceRegistrar implements BeanRegistrar {

    /**
     * Registers the appropriate {@code MessageService} implementation
     * based on application configuration.
     *
     * <p>
     * Unlike traditional approaches, this method avoids manual bean definition
     * creation and interacts with the container using a higher-level,
     * intention-revealing API.
     * </p>
     *
     * @param registry the {@link BeanRegistry} used to register beans programmatically
     * @param env      the Spring {@link Environment} providing access to configuration properties
     */
    @Override
    public void register(BeanRegistry registry, Environment env) {
        String messageType = env.getProperty("app.message-type", "email");

        switch (messageType.toLowerCase()) {
            case "email" -> registry.registerBean("messageService", EmailMessageService.class, spec -> spec
                    .description("Email message service registered via BeanRegistrar"));
            case "sms" -> registry.registerBean("messageService", SmsMessageService.class, spec -> spec
                    .description("SMS message service registered via BeanRegistrar"));
            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }
}