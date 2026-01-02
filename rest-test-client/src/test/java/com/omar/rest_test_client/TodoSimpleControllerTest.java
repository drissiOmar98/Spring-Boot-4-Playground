package com.omar.rest_test_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoSimpleControllerTest {

    RestTestClient client;

    @BeforeEach
    void setup() {
        client = RestTestClient.bindToController(new TodoSimpleController()).build();
    }

    @Test
    void findAllTodos() {
        List<Todo> todos = client.get()
                .uri("/api/todos/simple/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                .returnResult()
                .getResponseBody();

        assertEquals(1, todos.size());
        assertEquals("First Todo", todos.getFirst().title());
    }

    @Test
    void findTodoById() {
        var todo = new Todo(1L, 1L, "First Todo", true);
        client.get()
                .uri("/api/todos/simple/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .isEqualTo(todo)
                .value( t -> { // check values
                    assertEquals(1L,t.id());
                    assertEquals(1L,t.userId());
                    assertEquals("First Todo",t.title());
                    assertEquals(true,t.completed());
                });
    }

    /*
     * Using AssertJ Assertions
     */
    @Test
    void findTodoByIdAssertJ() {
        client.get()
                .uri("/api/todos/simple/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .value(todo -> {
                    assertThat(todo.id()).isEqualTo(1L);
                    assertThat(todo.title()).isEqualTo("First Todo");
                    assertThat(todo.completed()).isTrue();
                });
    }


}