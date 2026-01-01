package com.omar.http_interfaces_demo.todo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Traditional, manual HTTP client for the "todos" endpoint.
 *
 * <p>This class demonstrates how HTTP calls were made before HTTP Interfaces:
 * using a low-level RestClient to perform GET, POST, PUT, DELETE operations manually.
 *
 * <p>It requires explicit URI building, response deserialization, and boilerplate
 * for each method.
 *
 * <p>Purpose:
 * <ul>
 *   <li>Teaching comparison with modern HTTP interfaces</li>
 *   <li>Showing internal flow: RestClient → HTTP request → Response → Java object</li>
 * </ul>
 */
public class TraditionalTodoService {

    private final RestClient client;

    public TraditionalTodoService(RestClient.Builder builder) {
        this.client = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public List<Todo> findAll() {
        return client.get()
                .uri("/todos")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Todo findById(Integer id) {
        return client.get()
                .uri("/todos/{id}", id)
                .retrieve()
                .body(Todo.class);
    }

    public Todo create(Todo todo) {
        return client.post()
                .uri("/todos")
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public Todo update(Integer id, Todo todo) {
        return client.put()
                .uri("/todos/{id}", id)
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public void delete(Integer id) {
        client.delete()
                .uri("/todos/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}