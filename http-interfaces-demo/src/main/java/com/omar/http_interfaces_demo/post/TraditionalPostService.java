package com.omar.http_interfaces_demo.post;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class TraditionalPostService {

    private final RestClient client;

    public TraditionalPostService(RestClient.Builder builder) {
        this.client = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public List<Post> findAll() {
        return client.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Post findById(Integer id) {
        return client.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }

    public Post create(Post post) {
        return client.post()
                .uri("/posts")
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    public Post update(Integer id, Post post) {
        return client.put()
                .uri("/posts/{id}", id)
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    public void delete(Integer id) {
        client.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}