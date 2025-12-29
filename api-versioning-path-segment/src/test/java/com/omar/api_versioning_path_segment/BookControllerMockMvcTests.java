package com.omar.api_versioning_path_segment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerMockMvcTests {
    @Autowired
    MockMvcTester mvc;

    @Test
    void shouldSearchBooksUsingV1() {
        MvcTestResult testResult = mvc.get().uri("/api/{version}/books/search?q=007", "v1.0")
                .exchange();

        assertThat(testResult)
                .hasStatusOk()
                .bodyJson()
                .convertTo(Books.class)
                .satisfies(books -> {
                    assertThat(books.books()).hasSize(1);
                    assertThat(books.books().getFirst().getIsbn()).isEqualTo("978-0-14-028008-4");
                });

    }

    @Test
    void shouldSearchBooksUsingV2() {
        MvcTestResult testResult = mvc.get()
                .uri("/api/{version}/books/search?q=007", "v2")
                .exchange();

        assertThat(testResult)
                .hasStatusOk()
                .bodyJson()
                .convertTo(Books.class)
                .satisfies(books -> {
                    assertThat(books.books()).hasSize(2);
                    assertThat(books.books())
                            .extracting("isbn")
                            .containsExactlyInAnyOrder("978-0-14-028008-4", "978-0-14-028007-3");
                });
    }

}