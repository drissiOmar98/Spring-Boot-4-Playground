package com.omar.spring_ai_2_demo.google;

import com.google.genai.Client;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleGenAiConfig {

    @Bean
    public GoogleGenAiChatModel googleGenAiChatModel() {
        var apiKey = System.getenv("GOOGLE_GENAI_API_KEY");
        return GoogleGenAiChatModel.builder()
            .genAiClient(Client.builder()
                .apiKey(apiKey)
                .build())
            .defaultOptions(GoogleGenAiChatOptions.builder()
                .model("gemini-2.0-flash")
                .build())
            .build();
    }
}