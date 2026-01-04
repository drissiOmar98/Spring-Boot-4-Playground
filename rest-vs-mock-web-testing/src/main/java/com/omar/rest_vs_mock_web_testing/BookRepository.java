package com.omar.rest_vs_mock_web_testing;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookRepository {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BookRepository() {
        // Pre-populate with sample data
        save(new Book("Fundamentals of Software Engineering", List.of("Nathaniel Schutta", "Dan Vega"), "978-1098143237", 2025));
        save(new Book("Clean Code", "Robert C. Martin", "978-0132350884", 2008));
        save(new Book("Effective Java", "Joshua Bloch", "978-0134685991", 2018));
        save(new Book("Domain-Driven Design", "Eric Evans", "978-0321125217", 2003));
        save(new Book("Clean Architecture", "Robert C. Martin", "978-0134494166", 2017));
        save(new Book("Refactoring", "Martin Fowler", "978-0134757599", 2018));
        save(new Book("The Pragmatic Programmer", "David Thomas", "978-0135957059", 2019));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book save(Book book) {
        if (book.id() == null) {
            Long id = idGenerator.getAndIncrement();
            Book newBook = new Book(id, book.title(), book.authors(), book.isbn(), book.publishedYear());
            books.put(id, newBook);
            return newBook;
        }
        books.put(book.id(), book);
        return book;
    }

    public boolean existsById(Long id) {
        return books.containsKey(id);
    }

    public void deleteById(Long id) {
        books.remove(id);
    }

    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        String lowerAuthor = author.toLowerCase();
        return books.values().stream()
                .filter(book -> book.authors().stream()
                        .anyMatch(a -> a.toLowerCase().contains(lowerAuthor)))
                .toList();
    }

    public List<Book> findByPublishedYearGreaterThanEqual(Integer year) {
        return books.values().stream()
                .filter(book -> book.publishedYear() != null && book.publishedYear() >= year)
                .toList();
    }

    public List<Book> findByTitleContainingIgnoreCase(String title) {
        String lowerTitle = title.toLowerCase();
        return books.values().stream()
                .filter(book -> book.title().toLowerCase().contains(lowerTitle))
                .toList();
    }
}


