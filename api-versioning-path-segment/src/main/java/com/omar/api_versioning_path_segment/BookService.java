package com.omar.api_versioning_path_segment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class BookService {
    private final List<Book> BOOKS = List.of(
        new Book("978-0-14-028005-1", "The Great Gatsby", "F. Scott Fitzgerald"),
        new Book("978-0-14-028006-2", "To Kill a Mockingbird", "Harper Lee"),
        new Book("978-0-14-028007-3", "Pride and Prejudice", "Jane Austen"),
        new Book("978-0-14-028008-4", "James Bond 007", "Ian Fleming")
    );


    public Books getAllBooks() {
        return new Books(BOOKS);
    }


    public Optional<Book> getBookByIsbn(String isbn) {
        return BOOKS.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    public Books searchByTitle(String query) {
        return new Books(BOOKS.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                .toList());
    }


    public Books searchBooks(String query) {
        return new Books(BOOKS.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase())
                            || book.getAuthor().toLowerCase().contains(query.toLowerCase())
                            || book.getIsbn().toLowerCase().contains(query.toLowerCase()))
                .toList());
    }
}