package com.omar.rest_test_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest // Full Spring context, but NO server by default
class TodoControllerContextTest {

    @Autowired
    private WebApplicationContext context;

    RestTestClient client;
    @Autowired
    private TodoService todoService;

    @BeforeEach
    void setup(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void findAllTodos() {
        List<Todo> todos = client.get()
                .uri("/api/todos/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                .returnResult()
                .getResponseBody();

        assertEquals(200, todos.size());
        assertEquals("delectus aut autem", todos.get(0).title());
    }


}