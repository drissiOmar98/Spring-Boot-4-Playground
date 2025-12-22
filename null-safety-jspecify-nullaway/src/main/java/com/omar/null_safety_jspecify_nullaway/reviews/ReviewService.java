package com.omar.null_safety_jspecify_nullaway.reviews;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {

    // ============================================
    // 3. COLLECTIONS WITH NULLABLE ELEMENTS
    // ============================================

    // List itself is non-null (We marked the package as @NullMarked), but can contain null elements
    // Customer might skip some survey questions
    public List<@Nullable String> getResponses() {
        List<@Nullable String> responses = new ArrayList<>();
        responses.add("Excellent service");      // Question 1: answered
        responses.add(null);                     // Question 2: skipped
        responses.add("Coffee was too hot");     // Question 3: answered
        responses.add(null);                     // Question 4: skipped
        responses.add("Would visit again");      // Question 5: answered
        return responses;
    }

    // Processing survey data - nulls represent unanswered questions
    public int calculateResponseRate(List<@Nullable String> responses) {
        long answered = responses.stream()
                .filter(Objects::nonNull)
                .count();
        return (int) ((answered * 100) / responses.size());
    }
}