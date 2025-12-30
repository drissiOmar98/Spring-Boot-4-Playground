package com.omar.api_versioning_header;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
class BookController {
    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    Books getAll() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    ResponseEntity<Book> getByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/search", version = "1.0")
    Books searchBooks(@RequestParam("q") String query) {
        return bookService.searchByTitle(query);
    }

    @GetMapping(value = "/search", version = "2.0")
    Books searchBooks_V2(@RequestParam("q") String query) {
        return bookService.searchBooks(query);
    }
}