package com.omar.rest_vs_mock_web_testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.web.servlet.client.assertj.RestTestClientResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Controller tests using RestTestClient - Spring Framework 7's unified REST testing API.
 *
 * <h2>When to Choose RestTestClient</h2>
 * <ul>
 *   <li><strong>You want API consistency</strong> - Same API for mock AND real HTTP tests</li>
 *   <li><strong>You need broader deserialization</strong> - Any HttpMessageConverter (XML, Protobuf, etc.)</li>
 *   <li><strong>You already know WebTestClient</strong> - Familiar fluent API</li>
 * </ul>
 *
 *
 * <p>With @WebMvcTest, it uses mock infrastructure (bindTo MockMvc).
 * With @SpringBootTest, it auto-switches to mock or running server depending on your config.</p>
 *
 * <h2>Current Limitation</h2>
 * <p>RestTestClient doesn't support multipart requests yet (Spring issue #35569).</p>
 *
 * @see BookControllerMockMvcTesterTest for the MockMvcTester alternative
 */
@WebMvcTest(BookController.class)
@AutoConfigureRestTestClient
class BookControllerRestTestClientTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    BookRepository bookRepository;

    @Test
    @DisplayName("GET /api/books - should return all books")
    void shouldReturnAllBooks() {
        var books = List.of(
                new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025),
                new Book(2L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018)
        );
        when(bookRepository.findAll()).thenReturn(books);

        client.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("GET /api/books - should return empty list when no books exist")
    void shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        client.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);
    }

    @Test
    @DisplayName("GET /api/books/{id} - should return book when found")
    void shouldReturnBookWhenFound() {
        var book = new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        client.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Fundamentals of Software Engineering")
                .jsonPath("$.authors[0]").isEqualTo("Nathaniel Schutta");
    }

    @Test
    @DisplayName("GET /api/books/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        client.get().uri("/api/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("GET /api/books/search - should search books by title")
    void shouldSearchByTitle() {
        var books = List.of(
                new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025)
        );
        when(bookRepository.findByTitleContainingIgnoreCase("fundamentals")).thenReturn(books);

        client.get().uri("/api/books/search?title=fundamentals")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].authors[0]").isEqualTo("Nathaniel Schutta");
    }

    @Test
    @DisplayName("GET /api/books/author/{author} - should find books by author")
    void shouldFindByAuthor() {
        var books = List.of(
                new Book(1L, "Clean Code", "Robert Martin", "978-0132350884", 2008),
                new Book(2L, "Clean Architecture", "Robert Martin", "978-0134494166", 2017)
        );
        when(bookRepository.findByAuthorContainingIgnoreCase("martin")).thenReturn(books);

        client.get().uri("/api/books/author/martin")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("POST /api/books - should create a new book")
    void shouldCreateBook() {
        var newBook = new Book(1L, "Domain-Driven Design", "Eric Evans", "978-0321125217", 2003);
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        client.post().uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "title": "Domain-Driven Design",
                            "authors": ["Eric Evans"],
                            "isbn": "978-0321125217",
                            "publishedYear": 2003
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Domain-Driven Design");
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - should delete existing book")
    void shouldDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        client.delete().uri("/api/books/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - should return 404 when deleting non-existent book")
    void shouldReturn404WhenDeletingNonExistent() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        client.delete().uri("/api/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    // =========================================================================
    // UNIQUE RESTTESTCLIENT CAPABILITIES
    // =========================================================================


    /**
     * Demonstrates that RestTestClient can deserialize a single response
     * into a typed domain object.
     *
     * <p>This is useful when you want a strongly-typed object directly from
     * the response without manually mapping JSON.</p>
     */
    @Test
    @DisplayName("RestTestClient can deserialize response to typed object")
    void canDeserializeToTypedObject() {
        var book = new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        client.get().uri("/api/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(b -> {
                    assert b.title().equals("Fundamentals of Software Engineering");
                    assert b.authors().contains("Dan Vega");
                });
    }

    /**
     * Demonstrates that RestTestClient can deserialize a response containing
     * a list of objects into a typed List.
     *
     * <p>This allows for strongly-typed collection assertions without manual parsing.</p>
     */
    @Test
    @DisplayName("RestTestClient can deserialize response to typed list")
    void canDeserializeToTypedList() {
        var books = List.of(
                new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025),
                new Book(2L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018)
        );
        when(bookRepository.findAll()).thenReturn(books);

        client.get().uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Book>>() {})
                .value( b -> {
                    assert b.get(0).title().equals("Fundamentals of Software Engineering");
                    assert b.get(1).authors().contains("Joshua Bloch");
                });
    }


    /**
     * Tests that RestTestClient can handle multiple authors correctly
     * when searching by author.
     *
     * <p>Verifies that JSON response contains all authors and correct
     * book information.</p>
     */
    @Test
    @DisplayName("GET /api/books/author/{author} - should find books with multiple authors")
    void shouldFindBooksWithMultipleAuthors() {
        var book = new Book(1L, "Fundamentals of Software Engineering",
                List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findByAuthorContainingIgnoreCase("vega")).thenReturn(List.of(book));

        client.get().uri("/api/books/author/vega")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Fundamentals of Software Engineering")
                .jsonPath("$[0].authors.length()").isEqualTo(2)
                .jsonPath("$[0].authors[0]").isEqualTo("Nathaniel Schutta")
                .jsonPath("$[0].authors[1]").isEqualTo("Dan Vega");
    }

    /**
     * Demonstrates that RestTestClient can be wrapped with
     * RestTestClientResponse to use AssertJ-style fluent assertions.
     *
     * <p>This enables consistency with MockMvcTester-style assertion chains
     * in your tests.</p>
     */
    @Test
    @DisplayName("RestTestClient supports AssertJ-style assertions via RestTestClientResponse")
    void canUseAssertJStyleAssertions() {
        var book = new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        var spec = client.get().uri("/api/books/1").exchange();
        var response = RestTestClientResponse.from(spec);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Fundamentals of Software Engineering");
    }
}
