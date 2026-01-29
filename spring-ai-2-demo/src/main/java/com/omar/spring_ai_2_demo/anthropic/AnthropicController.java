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

    /**
     * Generates downloadable documents using Anthropic Claude Skills API.
     *
     * <p>NEW IN Spring AI 2.0:</p>
     * <ul>
     *     <li>Skills API: Claude can generate documents in multiple formats.</li>
     *     <li>Supported types: excel (XLSX), powerpoint (PPTX), word (DOCX), pdf (PDF).</li>
     *     <li>Useful for automated document creation, reporting, or exporting AI responses.</li>
     * </ul>
     *
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> /api/anthropic/skills/{type}</p>
     *
     * @param type the type of document to generate: "excel", "powerpoint", "word", or "pdf"
     * @param request the {@link SkillRequest} containing:
     * <ul>
     *     <li>{@code prompt} - the instruction or content for Claude to generate the document</li>
     * </ul>
     * @return a {@link SkillResponse} containing:
     * <ul>
     *     <li>{@code answer} - AI-generated text from the document</li>
     *     <li>{@code fileIds} - identifiers of the generated files</li>
     *     <li>{@code type} - the requested document type</li>
     * </ul>
     * @throws IllegalArgumentException if the {@code type} is not one of the supported formats
     */
    @PostMapping("/skills/{type}")
    public SkillResponse generateDocument(
            @PathVariable String type,
            @RequestBody SkillRequest request
    ) {
        // Map the type string to the corresponding AnthropicSkill enum
        AnthropicApi.AnthropicSkill skill = switch (type.toLowerCase()) {
            case "excel" -> AnthropicApi.AnthropicSkill.XLSX;
            case "powerpoint" -> AnthropicApi.AnthropicSkill.PPTX;
            case "word" -> AnthropicApi.AnthropicSkill.DOCX;
            case "pdf" -> AnthropicApi.AnthropicSkill.PDF;
            default -> throw new IllegalArgumentException("Supported types: excel, powerpoint, word, pdf");
        };

        // Build the prompt and call Claude with the Skills API
        ChatResponse response = chatModel.call(
                new Prompt(
                        request.prompt(),   // The user-provided instruction
                        AnthropicChatOptions.builder()
                                .model("claude-sonnet-4-5") // Use Claude model optimized for Skills API
                                .maxTokens(16384) // Large token limit for document generation
                                .anthropicSkill(skill)  // Specify the document type
                                .build()
                )
        );

        // Return the response including AI-generated text and generated file IDs
        return new SkillResponse(
                response.getResult().getOutput().getText(), // AI-generated content
                extractFileIds(response),                   // IDs of generated files
                type                                        // Document type
        );
    }

    /**
     * Downloads a file generated by the Anthropic Skills API.
     *
     * <p>NEW IN Spring AI 2.0:</p>
     * <ul>
     *     <li>Supports downloading files (Excel, Word, PowerPoint, PDF) generated via the Skills API.</li>
     *     <li>Uses file metadata to set correct filename and content type.</li>
     * </ul>
     *
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> /api/anthropic/files/{fileId}</p>
     *
     * @param fileId the unique identifier of the generated file
     * @return a {@link ResponseEntity} containing the file content as a byte array,
     *         with proper Content-Disposition header for downloading
     */
    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {

        // Retrieve file metadata (filename, MIME type) from Anthropic API
        AnthropicApi.FileMetadata metadata = anthropicApi.getFileMetadata(fileId);

        // Download the file content as a byte array
        byte[] content = anthropicApi.downloadFile(fileId);

        // Build the HTTP response with proper headers for file download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metadata.filename() + "\"") // Suggest download filename
                .contentType(MediaType.parseMediaType(metadata.mimeType()))      // Set correct MIME type
                .body(content);                                                 // Return file content
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