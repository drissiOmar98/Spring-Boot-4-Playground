package com.omar.api_versioning_query_param;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import({RestTestClientConfig.class})
@AutoConfigureRestTestClient
class BookControllerRestTestClientTests {

    //@Autowired
    //private WebApplicationContext context;

    @Autowired
    RestTestClient client;

    /*
    @PostConstruct
    public void setup() {
        client = RestTestClient.bindToApplicationContext(context)
                    .apiVersionInserter(ApiVersionInserter.useHeader("API-Version"))
                    .build();
    }
    */

    @Test
    void shouldGetAllBooks() {
        Books books = client.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Books>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull();
        assertThat(books.books()).hasSize(4);
    }

    @Test
    void shouldSearchBooksUsingDefaultVersion() {
        Books books = client.get()
                .uri("/api/books/search?q=007")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Books>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull();
        assertThat(books.books()).hasSize(1);
        assertThat(books.books().getFirst().getIsbn()).isEqualTo("978-0-14-028008-4");
    }

    @Test
    void shouldSearchBooksUsingV2() {
        Books books = client.get()
                .uri("/api/books/search?q=007")
                .apiVersion("2.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Books>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(books).isNotNull();
        assertThat(books.books()).hasSize(2);
        assertThat(books.books())
                .extracting("isbn")
                .containsExactlyInAnyOrder("978-0-14-028008-4", "978-0-14-028007-3");
    }
}