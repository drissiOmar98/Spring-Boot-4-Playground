package com.omar.spring_ai_2_demo.redis;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * NEW IN SPRING AI 2.0: Redis Chat Memory Repository
 *
 * Demonstrates persistent conversation storage across sessions using Redis.
 * Features:
 * - Conversations persist across server restarts
 * - TTL support for automatic expiration
 * - Multiple concurrent conversations per user
 */
@RestController
@RequestMapping("/api/redis")
public class RedisChatMemoryController {

    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;

    /**
     * Constructs a RedisChatMemoryController with a ChatClient and ChatMemoryRepository.
     *
     * <p>This constructor demonstrates how to wire a ChatClient with Redis-based chat memory.
     * The memory persists conversations across server restarts and supports TTL-based expiration.</p>
     *
     * <p><b>Provider Note:</b></p>
     * <ul>
     *     <li>To use Google GenAI (Gemini), activate: {@code GoogleGenAiChatModel chatModel}</li>
     *     <li>To use OpenAI SDK instead, comment the Google model and uncomment: {@code OpenAiSdkChatModel chatModel}</li>
     * </ul>
     *
     * <p>The ChatClient is configured with a MessageChatMemoryAdvisor that wraps a Redis-backed
     * MessageWindowChatMemory with a max of 20 messages per conversation.</p>
     *
     * @param chatModel the AI chat model to use (Google Gemini or OpenAI SDK)
     * @param chatMemoryRepository the Redis-based chat memory repository
     */
    public RedisChatMemoryController(
//            OpenAiSdkChatModel chatModel,
            GoogleGenAiChatModel chatModel,
            ChatMemoryRepository chatMemoryRepository
    ) {
        this.chatMemoryRepository = chatMemoryRepository;

        // Create chat memory with Redis repository
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();

        // Build ChatClient with memory advisor
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }


}