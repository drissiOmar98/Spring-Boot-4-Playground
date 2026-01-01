package com.omar.http_interfaces_demo.config;


import com.omar.http_interfaces_demo.post.PostService;
import com.omar.http_interfaces_demo.todo.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * Modern HTTP Interface configuration using Spring Boot 4 and Spring Framework 7.
 *
 * <p>This configuration demonstrates the <strong>declarative approach</strong> to creating
 * HTTP clients using <em>HTTP Interfaces</em>. Instead of manually creating {@code RestClient}
 * and {@code HttpServiceProxyFactory} beans, Spring Boot automatically:
 *
 * <ul>
 *   <li>Discovers HTTP service interfaces</li>
 *   <li>Creates runtime proxy implementations</li>
 *   <li>Wires them as Spring beans</li>
 * </ul>
 *
 * <p>The {@link ImportHttpServices} annotation is the key trigger that tells Spring
 * which interfaces represent HTTP clients.
 *
 * <p>This configuration targets the <strong>JSONPlaceholder API</strong> and applies
 * a shared {@code RestClient} configuration to all imported HTTP services.
 */
@Configuration
@ImportHttpServices(types = {TodoService.class, PostService.class})
public class ModernConfig {


    /**
     * Configures the {RestClient} used by all imported HTTP service interfaces.
     *
     * <p>Spring Boot groups all HTTP services imported via {@link ImportHttpServices}
     * and provides a {RestClient.Builder} for each service. This callback allows
     * customizing the builder <strong>before</strong> the HTTP service proxies are created.
     *
     * <p>In this example:
     * <ul>
     *   <li>All HTTP interfaces are part of the default group</li>
     *   <li>A common base URL is applied</li>
     * </ul>
     *
     * <p>This eliminates the need for:
     * <ul>
     *   <li>Explicit {@code RestClient} beans</li>
     *   <li>{@code HttpServiceProxyFactory} configuration</li>
     *   <li>Manual client instantiation</li>
     * </ul>
     *
     * @return a {@link RestClientHttpServiceGroupConfigurer} that customizes all HTTP clients
     */

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> groups.forEachClient((group, builder) ->
                // Apply the same RestClient configuration to every HTTP service client
                builder
                        // Base URL for the JSONPlaceholder public API
                        .baseUrl("https://jsonplaceholder.typicode.com/")
                        // Finalize the RestClient configuration
                        .build());
    }


    /*
     * -------------------------------------------------------------------------
     * Advanced example: configuring multiple HTTP service groups
     * -------------------------------------------------------------------------
     *
     * This example demonstrates how different HTTP interfaces can be grouped
     * and configured independently.
     *
     * Example usage:
     *
     * @ImportHttpServices(group = "jsonplaceholder",
     *     types = { TodoService.class, PostService.class })
     *
     * @ImportHttpServices(group = "github",
     *     types = { GithubRepositoryService.class })
     *
     * Each group can then be configured with a different base URL,
     * authentication mechanism, or default headers.
     */

    /*
     * Example configuring multiple groups
     * @ImportHttpServices(group = "jsonplaceholder", types = {TodoService.class, PostService.class})
     */

    /**
     * Example {@link RestClientHttpServiceGroupConfigurer} demonstrating
     * group-specific HTTP client configuration.
     *
     * <p>This is <strong>not active</strong> unless multiple
     * {@link ImportHttpServices} annotations with different group names are used.
     *
     * <p>Typical real-world use cases:
     * <ul>
     *   <li>Calling multiple external APIs</li>
     *   <li>Different authentication strategies per API</li>
     *   <li>Separate rate limits or headers</li>
     * </ul>
     *
     * @return a group-aware {@link RestClientHttpServiceGroupConfigurer}
     */
    RestClientHttpServiceGroupConfigurer multipleGroupConfigurer() {
        return groups -> {
            // Configuration applied only to HTTP services in the "github" group
            groups.filterByName("github")
                    .forEachClient((group, b) -> b
                            .baseUrl("https://api.github.com")
                            .defaultHeader("Accept",
                                    "application/vnd.github.v3+json"
                            )
                            .build()
                    );

            // Configuration applied only to HTTP services in the "jsonplaceholder" group
            groups.filterByName("jsonplaceholder")
                    .forEachClient((group, b) -> b
                            .baseUrl("https://jsonplaceholder.typicode.com/")
                            .build()
                    );
        };
    }

}