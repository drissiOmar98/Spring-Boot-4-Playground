package com.omar.dynamic_bean_registration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Modern Spring configuration that activates programmatic bean registration
 * via the {@link MessageServiceRegistrar}.
 * <p>
 * This configuration class demonstrates how Spring Boot 4 integrates
 * {@code BeanRegistrar} implementations using {@link Import}, eliminating
 * the need for low-level post-processors or complex conditional annotations.
 * </p>
 *
 * <p>
 * By importing the registrar, the application dynamically registers the
 * appropriate {@code MessageService} implementation at startup based on
 * environment properties.
 * </p>
 */
@Configuration
@Import(MessageServiceRegistrar.class)
public class ModernConfig {

    // other bean definitions here

}