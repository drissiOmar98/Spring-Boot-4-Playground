package com.omar.rest_vs_mock_web_testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Controller tests using MockMvcTester - Spring Framework 7's AssertJ-focused testing API.
 *
 * <h2>When to Choose MockMvcTester</h2>
 * <ul>
 *   <li><strong>You prefer AssertJ-style assertions</strong> - Natural assertThat() wrapping</li>
 *   <li><strong>You need server-side inspection</strong> - Access to handlers, exceptions, model attributes</li>
 *   <li><strong>You need multipart/file upload testing</strong> - RestTestClient doesn't support this yet</li>
 *   <li><strong>You need fine-grained request setup</strong> - Servlet path, request attributes, etc.</li>
 * </ul>
 *
 * @see BookControllerRestTestClientTest for the RestTestClient alternative
 */
@WebMvcTest(BookController.class)
class BookControllerMockMvcTesterTest {

    @Autowired
    MockMvcTester mockMvcTester;

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

        assertThat(mockMvcTester.get().uri("/api/books"))
                .hasStatusOk()
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(2);
    }

    @Test
    @DisplayName("GET /api/books - should return empty list when no books exist")
    void shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        assertThat(mockMvcTester.get().uri("/api/books"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .isEmpty();
    }

    @Test
    @DisplayName("GET /api/books/{id} - should return book when found")
    void shouldReturnBookWhenFound() {
        var book = new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThat(mockMvcTester.get().uri("/api/books/1"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Fundamentals of Software Engineering");
    }

    @Test
    @DisplayName("GET /api/books/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThat(mockMvcTester.get().uri("/api/books/999"))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /api/books/search - should search books by title")
    void shouldSearchByTitle() {
        var books = List.of(
                new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025)
        );
        when(bookRepository.findByTitleContainingIgnoreCase("fundamentals")).thenReturn(books);

        assertThat(mockMvcTester.get().uri("/api/books/search?title=fundamentals"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[0].authors[0]")
                .isEqualTo("Nathaniel Schutta");
    }

    @Test
    @DisplayName("POST /api/books - should create a new book")
    void shouldCreateBook() {
        var newBook = new Book(1L, "Domain-Driven Design", "Eric Evans", "978-0321125217", 2003);
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        String requestBody = """
                {
                    "title": "Domain-Driven Design",
                    "authors": ["Eric Evans"],
                    "isbn": "978-0321125217",
                    "publishedYear": 2003
                }
                """;

        assertThat(mockMvcTester.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Domain-Driven Design");
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - should delete existing book")
    void shouldDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertThat(mockMvcTester.delete().uri("/api/books/1"))
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - should return 404 when deleting non-existent book")
    void shouldReturn404WhenDeletingNonExistent() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThat(mockMvcTester.delete().uri("/api/books/999"))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // UNIQUE MOCKMVCTESTER CAPABILITIES
    // =========================================================================

    /**
     * Verifies that MockMvcTester can identify which controller handler method
     * was invoked for a given request.
     *
     * <p>This is useful for tests where you want to assert server-side routing
     * behavior or inspect the invoked handler directly.</p>
     */
    @Test
    @DisplayName("MockMvcTester can verify which handler method was invoked")
    void canVerifyHandlerMethod() {
        when(bookRepository.findAll()).thenReturn(List.of());

        var result = mockMvcTester.get().uri("/api/books");

        // Assert HTTP status and verify invoked handler method details
        assertThat(result)
                .hasStatusOk()
                .apply(mvcResult -> assertThat((org.springframework.web.method.HandlerMethod) mvcResult.getHandler())
                        .isNotNull()
                        .satisfies(handler -> {
                            assertThat(handler.getMethod().getName()).isEqualTo("findAll");
                            assertThat(handler.getBeanType()).isEqualTo(BookController.class);
                        }));
    }

    /**
     * Demonstrates fluent JSON path assertions with MockMvcTester.
     *
     * <p>Shows how AssertJ-style chaining can be used to inspect JSON responses
     * without manually parsing or mapping the payload.</p>
     */
    @Test
    @DisplayName("MockMvcTester provides fluent JSON path assertions with chaining")
    void fluentJsonPathAssertions() {
        var books = List.of(
                new Book(1L, "Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025),
                new Book(2L, "Effective Java", "Joshua Bloch", "978-0134685991", 2018)
        );
        when(bookRepository.findAll()).thenReturn(books);

        var result = mockMvcTester.get().uri("/api/books");

        assertThat(result).hasStatusOk();
        // Assert JSON body using path extraction
        assertThat(result).bodyJson().extractingPath("$[0].title").isEqualTo("Fundamentals of Software Engineering");
        assertThat(result).bodyJson().extractingPath("$[1].authors[0]").isEqualTo("Joshua Bloch");
    }

    /**
     * Tests that MockMvcTester correctly handles books with multiple authors.
     *
     * <p>Verifies that search by author returns all matching books and
     * correctly exposes multiple authors in the JSON response.</p>
     */
    @Test
    @DisplayName("GET /api/books/author/{author} - should find books with multiple authors")
    void shouldFindBooksWithMultipleAuthors() {
        var book = new Book(1L, "Fundamentals of Software Engineering",
                List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025);
        when(bookRepository.findByAuthorContainingIgnoreCase("vega")).thenReturn(List.of(book));

        var result = mockMvcTester.get().uri("/api/books/author/vega");

        assertThat(result).hasStatusOk();
        // Assert JSON body structure and content
        assertThat(result).bodyJson().extractingPath("$[0].title").isEqualTo("Fundamentals of Software Engineering");
        assertThat(result).bodyJson().extractingPath("$[0].authors").asArray().hasSize(2);
        assertThat(result).bodyJson().extractingPath("$[0].authors[0]").isEqualTo("Nathaniel Schutta");
        assertThat(result).bodyJson().extractingPath("$[0].authors[1]").isEqualTo("Dan Vega");
    }
}


