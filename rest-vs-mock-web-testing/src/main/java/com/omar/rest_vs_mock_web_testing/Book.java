package com.omar.rest_vs_mock_web_testing;

import java.util.List;

public record Book(
        Long id,
        String title,
        List<String> authors,
        String isbn,
        Integer publishedYear
) {
    public Book {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors cannot be empty");
        }
    }

    public Book(String title, List<String> authors, String isbn, Integer publishedYear) {
        this(null, title, authors, isbn, publishedYear);
    }

    public Book(String title, String author, String isbn, Integer publishedYear) {
        this(null, title, List.of(author), isbn, publishedYear);
    }

    public Book(Long id, String title, String author, String isbn, Integer publishedYear) {
        this(id, title, List.of(author), isbn, publishedYear);
    }
}
