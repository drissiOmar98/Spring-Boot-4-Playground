package com.omar.api_versioning_demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // USING PATH SEGMENT ======================================================

    @GetMapping(value = "/{version}/users", version = "1.0")
    public List<User> findAllv1() {
        log.info("Finding all users v1");
        return userRepository.findAll();
    }

    @GetMapping(value = "/{version}/users", version = "2.0")
    public List<User> findAllv2() {
        log.info("Finding all users v2");
        return userRepository.findAll();
    }



}