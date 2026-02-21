package com.omar.jackson3_json_mapping.client;

import com.fasterxml.jackson.annotation.JsonView;
import com.omar.jackson3_json_mapping.model.Donut;
import com.omar.jackson3_json_mapping.model.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 🖥️ ClientApp demonstrates how a Spring Boot 4 application
 * can use {@link RestClient} with Jackson 3 {@link JsonView}
 * to selectively serialize fields when calling a REST API.
 *
 * <p>Specifically, this client:</p>
 * <ul>
 *   <li>🍩 Creates a Donut with all fields populated</li>
 *   <li>👁️ Sends only Summary fields using {@code hint(JsonView)}</li>
 *   <li>📡 Demonstrates client-side JSON Views filtering</li>
 * </ul>
 */
public class ClientApp implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ClientApp.class);
    private final RestClient client;

    public ClientApp(RestClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApp.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    /**
     * Application startup logic.
     *
     * @param args Application arguments (ignored)
     * @throws Exception any error during REST call
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create a Donut with ALL fields populated
        Donut clientDonut = new Donut(
                "Maple Bar",
                Donut.Glaze.MAPLE,
                List.of("pecans", "bacon"),
                new BigDecimal("3.99"),
                false,
                450,  // Client tries to set calories
                LocalDateTime.now().minusHours(2)  // Client tries to set bakedAt
        );

        log.info("\nUsing hint() with Views.Summary.class - only type and price will be sent");

        // POST with hint() - only Summary fields (type, price) are serialized and sent
        this.client.post()
                .uri("/api/donuts")
                .hint(JsonView.class.getName(), Views.Summary.class)
                .body(clientDonut)
                .retrieve()
                .body(Donut.class);
    }
}