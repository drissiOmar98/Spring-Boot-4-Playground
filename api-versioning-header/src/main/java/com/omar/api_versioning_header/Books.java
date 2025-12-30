package com.omar.api_versioning_header;

import java.util.List;

record Books(List<Book> books){
    public static Books of(List<Book> books) {
            return new Books(books);
        }
}