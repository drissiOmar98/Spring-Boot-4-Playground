package com.omar.api_versioning_path_segment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
class BookControllerRestTestClientTests {

    @Autowired
    RestTestClient client;

    @Test
    void shouldSearchBooksUsingV1() {
        Books books = client.get()
                .uri("/api/{version}/books/search?q=007", "1.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Books>() {})
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull();
        assertThat(books.books()).hasSize(1);
        assertThat(books.books().getFirst().getIsbn()).isEqualTo("978-0-14-028008-4");
    }

    @Test
    void shouldSearchBooksUsingV2() {
        Books books = client.get()
                .uri("/api/{version}/books/search?q=007", "2.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Books>() {})
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull();
        assertThat(books.books()).hasSize(2);
        assertThat(books.books())
                .extracting("isbn")
                .containsExactlyInAnyOrder("978-0-14-028008-4", "978-0-14-028007-3");
    }
}