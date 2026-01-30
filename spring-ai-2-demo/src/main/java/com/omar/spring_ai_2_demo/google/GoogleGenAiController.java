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


    /**
     * Performs a chat request and returns safety ratings metadata.
     *
     * <p>NEW IN Spring AI 2.0:</p>
     * Gemini responses can include safety classification information,
     * such as toxicity, violence, or sensitive content indicators.
     *
     * <p>This endpoint demonstrates how to extract and expose this metadata.</p>
     *
     * <p>HTTP Method: POST</p>
     * <p>Endpoint: /api/google/chat/safe</p>
     *
     * @param request contains the user's message
     * @return the generated response and associated safety ratings
     */
    @PostMapping("/chat/safe")
    public SafeResponse chatWithSafety(@RequestBody ChatRequest request) {
        // Execute prompt and retrieve full chat response (with metadata)
        var response = chatClient.prompt()
                .user(request.message())
                .call()
                .chatResponse();

        // Extract main response text
        String content = response.getResult().getOutput().getText();

        // Extract safety ratings from metadata if available
        @SuppressWarnings("unchecked")
        var safetyRatings = response.getMetadata().get("safetyRatings");

        // Return response and safety information
        return new SafeResponse(
                content,
                safetyRatings != null ? safetyRatings : List.of()
        );
    }


    /**
     * Performs a chat request using Gemini "thinking mode".
     *
     * <p>NEW IN Spring AI 2.0:</p>
     * Gemini 2.5 Pro supports extended reasoning through a "thinking budget".</p>
     *
     * <p>Thinking Budget Options:</p>
     * <ul>
     *     <li>-1 → Dynamic (model decides)</li>
     *     <li>0 → Disable thinking</li>
     *     <li>>0 → Fixed token budget</li>
     * </ul>
     *
     * <p>This mode is useful for complex reasoning, planning,
     * and multi-step problem solving.</p>
     *
     * <p>HTTP Method: POST</p>
     * <p>Endpoint: /api/google/chat/think</p>
     *
     * @param request contains the user message and optional thinking budget
     * @return response text, extracted thoughts, and used budget
     */
    @PostMapping("/chat/think")
    public ThinkingResponse chatWithThinking(@RequestBody ThinkingRequest request) {
        // Default to dynamic thinking (-1), or use provided budget
        int budget = request.thinkingBudget() != null ? request.thinkingBudget() : -1;

        // Configure Gemini model with thinking mode enabled
        var options = GoogleGenAiChatOptions.builder()
                .model("gemini-2.5-pro")
                .thinkingBudget(budget)
                .includeThoughts(true)
                .build();

        // Execute prompt with advanced reasoning options
        var response = chatClient.prompt()
                .user(request.message())
                .options(options)
                .call()
                .chatResponse();

        // Extract main response content
        String content = response.getResult().getOutput().getText();

        // Extract thinking/reasoning from metadata if available
        var thoughts = response.getMetadata().get("thoughts");

        return new ThinkingResponse(
                content,
                thoughts != null ? thoughts.toString() : null,
                budget
        );
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