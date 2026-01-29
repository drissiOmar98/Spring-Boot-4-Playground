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


}