package com.omar.null_safety_jspecify_nullaway.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================================================
    // 1. Hidden NullPointerException risk (without enforcement)
    // =========================================================
    @GetMapping("/find-by-email")
    public User findByEmail(@RequestParam String email) {

        User user = userService.findUserByEmail(email);

        if (user.firstName().equalsIgnoreCase("omar")) {
            System.out.println(
                    "Omar is ordering an espresso ☕ — please prepare it!"
            );
        }

        return user;
    }

    // =========================================================
    // 2. Safe alternative using Optional
    // =========================================================
    @GetMapping("/find-by-email-optional")
    public ResponseEntity<User> findByEmailOptional(@RequestParam String email) {
        return userService.findUserByEmailOptional(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}