package com.omar.opentelemetry_demo;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Installs the OpenTelemetry Logback appender with the auto-configured
 * {@link OpenTelemetry} instance provided by Spring Boot.
 *
 * <p>
 * Spring Boot 4 auto-configures an {@code OpenTelemetry} SDK, but the
 * OpenTelemetry Logback appender does not automatically know which
 * instance to use. This component bridges that gap.
 * </p>
 *
 * <p>
 * Once the Spring context is initialized, this bean installs the
 * {@code OpenTelemetry} instance into the Logback appender, enabling
 * structured log export via OTLP.
 * </p>
 *
 * <p>
 * Result:
 * <ul>
 *   <li>Application logs are correlated with traces</li>
 *   <li>Logs are exported to the configured OTLP endpoint</li>
 *   <li>Seamless debugging in Grafana (logs ↔ traces)</li>
 * </ul>
 * </p>
 */
@Component
class InstallOpenTelemetryAppender implements InitializingBean {

    /**
     * Auto-configured OpenTelemetry SDK instance provided by Spring Boot.
     */
    private final OpenTelemetry openTelemetry;

    InstallOpenTelemetryAppender(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    /**
     * Installs the OpenTelemetry instance into the Logback appender
     * after all Spring beans have been initialized.
     */
    @Override
    public void afterPropertiesSet() {
        OpenTelemetryAppender.install(this.openTelemetry);
    }

}