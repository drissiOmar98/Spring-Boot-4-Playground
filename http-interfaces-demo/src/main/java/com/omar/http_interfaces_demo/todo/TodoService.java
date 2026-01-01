package com.omar.http_interfaces_demo.todo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

/**
 * Declarative HTTP client interface for the "todos" endpoint.
 *
 * <p>This interface demonstrates the <strong>Spring Boot 4 / Spring Framework 7</strong>
 * HTTP Interfaces feature. Spring automatically generates a runtime proxy that implements
 * this interface and translates method calls into HTTP requests.
 *
 * <p>Key features:
 * <ul>
 *   <li>{@link GetExchange}, {@link PostExchange}, {@link PutExchange}, {@link DeleteExchange} annotations map methods to HTTP verbs.</li>
 *   <li>{@link PathVariable} and {@link RequestBody} allow passing dynamic data.</li>
 *   <li>Type-safe responses ensure compile-time correctness.</li>
 * </ul>
 *
 * <p>This approach eliminates manual RestClient calls and boilerplate.
 */
@HttpExchange("/todos")
public interface TodoService {

    @GetExchange
    List<Todo> findAll();

    @GetExchange("/{id}")
    Todo findById(@PathVariable Integer id);

    @PostExchange
    Todo create(@RequestBody Todo todo);

    @PutExchange("/{id}")
    Todo update(@PathVariable Integer id, @RequestBody Todo todo);

    @DeleteExchange("/{id}")
    void delete(@PathVariable Integer id);

}