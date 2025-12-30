package com.omar.api_versioning_query_param;

import java.util.List;

record Books(List<Book> books){
    public static Books of(List<Book> books) {
            return new Books(books);
        }
}