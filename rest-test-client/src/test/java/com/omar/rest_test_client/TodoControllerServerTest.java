package com.omar.rest_test_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerServerTest {

    @LocalServerPort
    private int port;

    private RestTestClient client;

    @BeforeEach
    void setup() {
        client = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
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
        assertEquals("delectus aut autem", todos.getFirst().title());
        assertFalse(todos.getFirst().completed());
    }

    @Test
    void findTodoById() {
        client.get()
                .uri("/api/todos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .value(todo -> {
                    assertEquals(1L, todo.id());
                    assertEquals("delectus aut autem", todo.title());
                    assertFalse(todo.completed());
                    assertEquals(1L, todo.userId());
                });
    }

    @Test
    void testServerIsActuallyRunning() {
        // Verify the port is actually set (server is running)
        assertTrue(port > 0, "Server should be running on a port");
        assertNotEquals(8080, port, "Should be random port, not default");
    }
}