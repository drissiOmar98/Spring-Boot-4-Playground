package com.omar.spring_ai_2_demo.anthropic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AnthropicConfig {

    /*
     * Needed a longer timeout for the Anthropic Skills API demos
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30_000);  // 30 seconds
        factory.setReadTimeout(600_000);    // 10 minutes
        return RestClient.builder().requestFactory(factory);
    }
}