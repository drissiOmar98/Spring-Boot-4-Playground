package com.omar.http_interfaces_demo.post;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@HttpExchange("/posts")
public interface PostService {

    @GetExchange
    List<Post> findAll();

    @GetExchange("/{id}")
    Post findById(@PathVariable Integer id);

    @PostExchange
    Post create(@RequestBody Post post);

    @PutExchange("/{id}")
    Post update(@PathVariable Integer id, @RequestBody Post post);

    @DeleteExchange("/{id}")
    void delete(@PathVariable Integer id);

}