package com.omar.api_versioning_media_type_param;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import({MockMvcTestConfig.class})
@AutoConfigureMockMvc
class BookControllerMockMvcTests {
    @Autowired
    MockMvcTester mvc;

    @Test
    void shouldSearchBooksUsingDefaultVersion() {
        MvcTestResult testResult = mvc.get().uri("/api/books/search?q=007").exchange();

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
    void shouldSearchBooksUsingV1() {
        MvcTestResult testResult = mvc.get().uri("/api/books/search?q=007")
                .accept("application/json;ver=1.0")
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
    void shouldSearchBooksUsingV2WithMediaTypeParam() {
        MvcTestResult testResult = mvc.get()
                .uri("/api/books/search?q=007")
                .accept("application/json;ver=2.0")
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