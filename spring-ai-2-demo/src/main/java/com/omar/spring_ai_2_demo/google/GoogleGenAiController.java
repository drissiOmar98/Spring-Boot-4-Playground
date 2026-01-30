package com.omar.spring_ai_2_demo.google;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * NEW IN SPRING AI 2.0: Google GenAI (Gemini) Integration
 *
 * <p>This controller demonstrates how to integrate Google's Gemini models
 * using Spring AI 2.0 and the spring-ai-starter-model-google-genai starter.</p>
 *
 * Features in 2.0:
 * - Gemini 2.0 Flash model support
 * - Safety ratings in response metadata
 * - Thought signatures for function calling (Gemini 3 Pro)
 */
@RestController
@RequestMapping("/api/google")
public class GoogleGenAiController {

    private final ChatClient chatClient;

    public GoogleGenAiController(GoogleGenAiChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    /**
     * Performs a basic chat interaction with Google Gemini.
     *
     * <p>This endpoint sends the user's message to the Gemini model
     * and returns the generated response.</p>
     *
     * <p>HTTP Method: POST</p>
     * <p>Endpoint: /api/google/chat</p>
     *
     * @param request contains the user's message
     * @return a map containing the AI-generated response
     */
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest request) {
        String response = chatClient.prompt()
                .user(request.message())
                .call()
                .content();
        return Map.of("response", response);
    }



     /* ============================================================
       Request / Response DTOs
       ============================================================ */

    /**
     * Represents a basic chat request.
     *
     * @param message user input message
     */
    public record ChatRequest(String message) {}

    /**
     * Represents a chat request with optional thinking budget.
     *
     * @param message user input message
     * @param thinkingBudget optional reasoning token budget
     */
    public record ThinkingRequest(String message, Integer thinkingBudget) {}

    /**
     * Represents a response with safety ratings metadata.
     *
     * @param response generated AI text
     * @param safetyRatings safety classification details
     */
    public record SafeResponse(String response, Object safetyRatings) {}

    /**
     * Represents a response with extracted reasoning data.
     *
     * @param response generated AI text
     * @param thoughts internal reasoning output (if available)
     * @param thinkingBudget used token budget
     */
    public record ThinkingResponse(String response, String thoughts, int thinkingBudget) {}
}