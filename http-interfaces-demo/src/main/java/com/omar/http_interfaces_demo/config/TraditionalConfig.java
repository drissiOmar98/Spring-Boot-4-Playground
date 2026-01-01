package com.omar.http_interfaces_demo.config;


import com.omar.http_interfaces_demo.post.PostService;
import com.omar.http_interfaces_demo.todo.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Traditional HTTP Interface configuration using Spring Boot 4 and Spring Framework 7.
 *
 * <p>This configuration demonstrates the <strong>manual, explicit approach</strong>
 * for creating HTTP clients using the new Spring HTTP Interfaces feature.
 *
 * <p>Unlike the modern declarative approach (@ImportHttpServices), this style requires:
 * <ul>
 *   <li>Explicit creation of {@link RestClient} instances</li>
 *   <li>Manual configuration of {@link HttpServiceProxyFactory}</li>
 *   <li>Explicit creation of each HTTP interface client bean</li>
 * </ul>
 *
 * <p>This approach is useful for:
 * <ul>
 *   <li>Teaching or demonstrating the internals of Spring HTTP Interfaces</li>
 *   <li>Highly customized client configuration</li>
 *   <li>Legacy migrations from older HTTP client patterns</li>
 * </ul>
 */
//@Configuration
public class TraditionalConfig {

    // ---------------------------------------------------------------------------------
    // 1️⃣ RestClient Bean
    // ---------------------------------------------------------------------------------
    /**
     * Creates a {@link RestClient} instance pointing to the JSONPlaceholder API.
     *
     * <p>This is the low-level HTTP client that will be used by the proxy factory
     * to execute HTTP requests for all service interfaces.
     *
     * @return a configured {@link RestClient}
     */
    @Bean
    RestClient jsonplaceholderRestClient() {
        return RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }


    // ---------------------------------------------------------------------------------
    // 2️⃣ HttpServiceProxyFactory Bean
    // ---------------------------------------------------------------------------------
    /**
     * Creates an {@link HttpServiceProxyFactory} using the given {@link RestClient}.
     *
     * <p>The proxy factory is responsible for:
     * <ul>
     *   <li>Creating runtime proxies for all HTTP interfaces</li>
     *   <li>Mapping method calls to HTTP requests</li>
     *   <li>Handling response deserialization</li>
     * </ul>
     *
     * @param jsonplaceholderRestClient the low-level RestClient
     * @return a configured {@link HttpServiceProxyFactory}
     */
    @Bean
    HttpServiceProxyFactory jsonPlaceholderProxyFactory(RestClient jsonplaceholderRestClient) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(jsonplaceholderRestClient))
                .build();
    }

    // ---------------------------------------------------------------------------------
    // 3️⃣ Service Proxy Beans
    // ---------------------------------------------------------------------------------
    /**
     * Creates a {@link TodoService} HTTP client using the proxy factory.
     *
     * <p>This bean can be injected anywhere via {@code @Autowired} or constructor injection.
     * Calls to the interface methods are translated to HTTP requests automatically.
     *
     * @param jsonPlaceholderProxyFactory the proxy factory
     * @return a runtime proxy implementing {@link TodoService}
     */
    @Bean
    TodoService todoService(HttpServiceProxyFactory jsonPlaceholderProxyFactory) {
        return jsonPlaceholderProxyFactory.createClient(TodoService.class);
    }

    /**
     * Creates a {@link PostService} HTTP client using the proxy factory.
     *
     * <p>Same behavior as {@link #todoService(HttpServiceProxyFactory)}, but for posts.
     *
     * @param jsonPlaceholderProxyFactory the proxy factory
     * @return a runtime proxy implementing {@link PostService}
     */
    @Bean
    PostService postService(HttpServiceProxyFactory jsonPlaceholderProxyFactory) {
        return jsonPlaceholderProxyFactory.createClient(PostService.class);
    }

    // comments, albums, photos, users

}