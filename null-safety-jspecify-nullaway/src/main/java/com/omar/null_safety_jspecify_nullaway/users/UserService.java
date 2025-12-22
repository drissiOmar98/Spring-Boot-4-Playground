package com.omar.null_safety_jspecify_nullaway.users;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    List<User> users = new ArrayList<>();

    /**
     * Finds a user by email.
     *
     * <p>
     * ⚠️ This method may return {@code null} if no user matches the given email.
     * The {@link Nullable} annotation makes this contract explicit and allows
     * static analysis tools (JSpecify, NullAway) to enforce safe usage by callers.
     * </p>
     *
     * @param email the user's email address (must not be null)
     * @return the matching {@link User}, or {@code null} if no user is found
     */
    @Nullable public User findUserByEmail(String email) {
        return users.stream().filter(u -> u.email().equals(email)).findFirst().orElse(null);
    }

    /**
     * Finds a user by email using {@link Optional}.
     *
     * <p>
     * This alternative avoids {@code null} entirely and forces callers
     * to explicitly handle the absence of a value.
     * </p>
     *
     * @param email the user's email address (must not be null)
     * @return an {@link Optional} containing the user if found, otherwise empty
     */
    public Optional<User> findUserByEmailOptional(String email) {
        return users.stream()
                .filter(u -> u.email().equals(email))
                .findFirst();
    }

}