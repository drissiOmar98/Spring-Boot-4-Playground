package com.omar.spring_ai_2_demo.anthropic;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.Citation;
import org.springframework.ai.anthropic.SkillsResponseHelper;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.anthropic.api.CitationDocument;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NEW IN SPRING AI 2.0: Anthropic Claude Advanced Features
 *
 * Demonstrates two new Claude capabilities:
 * - Citations API: Get source references in responses
 * - Skills/Files API: Generate documents (Excel, PowerPoint, Word, PDF)
 *
 * https://docs.spring.io/spring-ai/reference/api/chat/anthropic-chat.html#_citations
 */
@RestController
@RequestMapping("/api/anthropic")
public class AnthropicController {

    private final AnthropicChatModel chatModel;
    private final ChatClient chatClient;
    private final AnthropicApi anthropicApi;

    public AnthropicController(AnthropicChatModel chatModel, AnthropicApi anthropicApi) {
        this.chatModel = chatModel;
        this.chatClient = ChatClient.builder(chatModel).build();
        this.anthropicApi = anthropicApi;
    }

    /**
     * Handles a request to the Anthropic Claude Citations API.
     *
     * <p>NEW IN Spring AI 2.0:</p>
     * <ul>
     *     <li>Citations API: Claude can reference specific parts of your document for fact-checking.</li>
     *     <li>Useful for validating AI responses against source content.</li>
     * </ul>
     *
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> /api/anthropic/citations</p>
     *
     * @param request the {@link CitationRequest} containing:
     * <ul>
     *     <li>{@code document} - the text document to be cited</li>
     *     <li>{@code title} - optional title of the document</li>
     *     <li>{@code question} - the user query to ask Claude about the document</li>
     * </ul>
     * @return a {@link CitationsResponse} containing:
     * <ul>
     *     <li>{@code answer} - AI-generated answer referencing the document</li>
     *     <li>{@code citations} - list of citations (title + URL) extracted from the response</li>
     * </ul>
     */
    @PostMapping("/citations")
    public CitationsResponse citations(@RequestBody CitationRequest request) {
        CitationDocument document = CitationDocument.builder()
                .plainText(request.document())
                .title(request.title())
                .citationsEnabled(true)
                .build();

        ChatResponse response = chatClient.prompt()
                .user(request.question())
                .options(AnthropicChatOptions.builder()
                        .model("claude-opus-4-5")
                        .citationDocuments(document)
                        .build())
                .call()
                .chatResponse();

        return new CitationsResponse(
                response.getResult().getOutput().getText(),
                extractCitations(response)
        );
    }



    @SuppressWarnings("unchecked")
    private List<Citation> extractCitations(ChatResponse response) {
        Object citations = response.getMetadata().get("citations");
        return citations instanceof List ? (List<Citation>) citations : List.of();
    }

    private List<String> extractFileIds(ChatResponse response) {
        return SkillsResponseHelper.extractFileIds(response);
    }

    public record CitationRequest(String document, String title, String question) {}
    public record CitationsResponse(String response, List<Citation> citations) {}
    public record SkillRequest(String prompt) {}
    public record SkillResponse(String response, List<String> fileIds, String type) {}
}