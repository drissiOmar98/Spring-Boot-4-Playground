package com.omar.dynamic_bean_registration.config;

import com.omar.dynamic_bean_registration.service.Impl.EmailMessageService;
import com.omar.dynamic_bean_registration.service.Impl.SmsMessageService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Traditional Spring configuration demonstrating dynamic bean registration
 * using {@link BeanDefinitionRegistryPostProcessor}.
 * <p>
 * This approach represents the "pre–Spring boot 4" way of conditionally
 * registering beans programmatically. While powerful, it requires more boilerplate
 * and lower-level interaction with the Spring container compared to the new
 * {@code BeanRegistrar} interface.
 * </p>
 *
 * <p>
 * The message service implementation (Email or SMS) is selected at startup
 * based on the {@code app.message-type} environment property.
 * </p>
 */
//@Configuration
public class TraditionalConfig {

    /**
     * Registers a {@link BeanDefinitionRegistryPostProcessor} that dynamically
     * creates and registers a {@code MessageService} bean based on application
     * configuration.
     *
     * <p>
     * This method illustrates how dynamic bean registration was traditionally
     * implemented using registry post-processors, requiring manual bean definition
     * creation and explicit registration.
     * </p>
     *
     * @param env the Spring {@link Environment} used to resolve configuration properties
     * @return a registry post-processor responsible for registering the message service bean
     */
    @Bean
    static BeanDefinitionRegistryPostProcessor messageServicePostProcessor(Environment env) {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
                // Resolve message type from configuration (default: email)
                String messageType = env.getProperty("app.message-type", "email");

                var beanDefinition = new GenericBeanDefinition();
                beanDefinition.setDescription("Traditional " + messageType + " message service");

                // Select the concrete implementation based on configuration
                switch (messageType.toLowerCase()) {
                    case "email" -> beanDefinition.setBeanClass(EmailMessageService.class);
                    case "sms" -> beanDefinition.setBeanClass(SmsMessageService.class);
                    default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
                }

                // Register the bean definition under a fixed, well-known name
                registry.registerBeanDefinition("messageService", beanDefinition);
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
                // No-op
            }
        };
    }
}