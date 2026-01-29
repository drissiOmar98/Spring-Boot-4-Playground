package com.omar.spring_ai_2_demo.openai;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.WebSearchTool;
import org.springframework.ai.openaisdk.OpenAiSdkChatOptions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * NEW IN SPRING AI 2.0: Official OpenAI Java SDK Integration
 *
 * Uses spring-ai-starter-model-openai-sdk which wraps the official OpenAI SDK.
 * Key benefits over the 1.x integration:
 * - Native Azure OpenAI & GitHub Models support
 * - Automatic API updates via SDK releases
 */
@RestController
@RequestMapping("/api/openai")
public class OpenAiSdkController {

    private final OpenAIClient openAIClient;

    public OpenAiSdkController(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_5_1)
                .addUserMessage(message)
                .build();

        ChatCompletion completion = openAIClient.chat().completions().create(params);
        return completion.choices().get(0).message().content().orElse("");
    }

    // ==================== RESPONSES API WITH WEB SEARCH ====================

    /**
     * Demonstrates OpenAI's Responses API with built-in web search.
     *
     * This is NOT available through Spring AI's ChatClient - it requires
     * direct SDK access. The Responses API is OpenAI's new primary API with:
     * - Built-in web search (model searches the web automatically)
     * - 40-80% better caching vs Chat Completions
     * - Built-in tools (web search, file search, code interpreter)
     */
    @PostMapping("/responses/search")
    public WebSearchResponse searchWithResponses(@RequestBody Map<String, String> request) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .input(request.get("query"))
                .model(ChatModel.GPT_5_1)
                .addTool(WebSearchTool.builder()
                        .type(WebSearchTool.Type.WEB_SEARCH)
                        .build())
                .build();

        Response response = openAIClient.responses().create(params);

        var outputTexts = response.output().stream()
                .filter(ResponseOutputItem::isMessage)
                .flatMap(item -> item.asMessage().content().stream())
                .filter(content -> content.isOutputText())
                .map(content -> content.asOutputText())
                .toList();

        String text = outputTexts.stream()
                .map(out -> out.text())
                .collect(Collectors.joining());

        List<Citation> citations = outputTexts.stream()
                .flatMap(out -> out.annotations().stream())
                .filter(annotation -> annotation.isUrlCitation())
                .map(annotation -> annotation.asUrlCitation())
                .map(c -> new Citation(c.title(), c.url()))
                .toList();

        return new WebSearchResponse(text, citations);
    }

    public record WebSearchResponse(String answer, List<Citation> citations) {}
    public record Citation(String title, String url) {}
}