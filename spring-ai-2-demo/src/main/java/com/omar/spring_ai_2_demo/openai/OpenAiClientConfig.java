package com.omar.spring_ai_2_demo.openai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to expose the OpenAI client for direct SDK access.
 *
 * This enables using OpenAI APIs not yet wrapped by Spring AI,
 * such as the Responses API with built-in web search.
 */
@Configuration
public class OpenAiClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.fromEnv();
    }
}