package com.omar.rest_vs_mock_web_testing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book not found with id: " + id));
    }

    @GetMapping("/search")
    public List<Book> searchByTitle(@RequestParam String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @GetMapping("/author/{author}")
    public List<Book> findByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @GetMapping("/recent")
    public List<Book> findRecentBooks(@RequestParam(defaultValue = "2020") Integer since) {
        return bookRepository.findByPublishedYearGreaterThanEqual(since);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}