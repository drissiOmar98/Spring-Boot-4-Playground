package com.omar.rest_test_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TodoControllerMockTest {

    RestTestClient client;
    TodoService todoService;

    @BeforeEach
    void setup() {
        // we have to mock this, no mockbean available because spring is not involved
        todoService = Mockito.mock(TodoService.class);
        when(todoService.findAll()).thenReturn(
                List.of(new Todo(1L, 1L, "First Todo", true))
        );
        client = RestTestClient.bindToController(new TodoController(todoService)).build();
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

        assertEquals(1, todos.size());
        assertEquals("First Todo", todos.getFirst().title());
    }


}