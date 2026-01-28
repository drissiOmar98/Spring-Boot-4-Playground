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

    /**
     * Handles a chat request with persistent memory using Redis.
     *
     * <p>Messages sent via this endpoint are stored in Redis, allowing conversations
     * to persist across server restarts and multiple sessions. Each conversation
     * is identified by a unique {@code conversationId}.</p>
     *
     * <p>Internally, the {@link ChatClient} is used with a {@link ChatMemory} advisor
     * to store and retrieve messages for the given conversation.</p>
     *
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> /api/redis/chat/{conversationId}</p>
     *
     * @param conversationId a unique identifier for the conversation; used to group messages in Redis
     * @param request the chat request containing the user's message
     * @return a {@link Map} containing:
     * <ul>
     *     <li>{@code conversationId} - the ID of the conversation</li>
     *     <li>{@code response} - the AI-generated reply for the given message</li>
     * </ul>
     */
    @PostMapping("/chat/{conversationId}")
    public Map<String, Object> chat(
            @PathVariable String conversationId,
            @RequestBody ChatRequest request
    ) {
        String response = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

        return Map.of(
                "conversationId", conversationId,
                "response", response
        );
    }

    /**
     * Retrieves the conversation history for a specific conversation.
     *
     * <p>This endpoint fetches all messages stored in Redis for the given {@code conversationId}.
     * It returns a structured history including message type and content, allowing clients
     * to display the full conversation or continue it.</p>
     *
     * <p>Internally, the {@link ChatMemoryRepository} is queried to retrieve messages
     * associated with the conversation.</p>
     *
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> /api/redis/history/{conversationId}</p>
     *
     * @param conversationId the unique identifier for the conversation whose history is being requested
     * @return a {@link Map} containing:
     * <ul>
     *     <li>{@code conversationId} - the ID of the conversation</li>
     *     <li>{@code messageCount} - the total number of messages in the conversation</li>
     *     <li>{@code messages} - a list of message objects, each containing:
     *         <ul>
     *             <li>{@code type} - the message type (e.g., USER, AI)</li>
     *             <li>{@code content} - the message text</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    @GetMapping("/history/{conversationId}")
    public Map<String, Object> getHistory(@PathVariable String conversationId) {
        var messages = chatMemoryRepository.findByConversationId(conversationId);

        List<Map<String, String>> history = messages.stream()
                .map(msg -> Map.of(
                        "type", msg.getMessageType().name(),
                        "content", msg.getText()
                ))
                .toList();

        return Map.of(
                "conversationId", conversationId,
                "messageCount", history.size(),
                "messages", history
        );
    }

    /**
     * Lists all conversation IDs currently stored in Redis.
     *
     * <p>This endpoint allows clients to retrieve a summary of all active or past conversations
     * maintained in Redis. Each conversation is identified by a unique {@code conversationId}.</p>
     *
     * <p>Internally, the {@link ChatMemoryRepository} is queried to fetch all conversation IDs.</p>
     *
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> /api/redis/conversations</p>
     *
     * @return a {@link Map} containing:
     * <ul>
     *     <li>{@code count} - the total number of conversations stored</li>
     *     <li>{@code conversationIds} - a list of all conversation IDs</li>
     * </ul>
     */
    @GetMapping("/conversations")
    public Map<String, Object> listConversations() {
        var conversationIds = chatMemoryRepository.findConversationIds();

        return Map.of(
                "count", conversationIds.size(),
                "conversationIds", conversationIds
        );
    }

    /**
     * Deletes a conversation and all its messages from Redis.
     *
     * <p>This endpoint allows clients to remove a specific conversation identified
     * by {@code conversationId}. All messages associated with this conversation
     * are deleted permanently from Redis.</p>
     *
     * <p>Internally, the {@link ChatMemoryRepository} is used to remove the conversation.</p>
     *
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> /api/redis/history/{conversationId}</p>
     *
     * @param conversationId the unique identifier of the conversation to delete
     * @return a {@link Map} containing:
     * <ul>
     *     <li>{@code conversationId} - the ID of the deleted conversation</li>
     *     <li>{@code deleted} - boolean indicating successful deletion</li>
     * </ul>
     */
    @DeleteMapping("/history/{conversationId}")
    public Map<String, Object> deleteHistory(@PathVariable String conversationId) {
        chatMemoryRepository.deleteByConversationId(conversationId);

        return Map.of(
                "conversationId", conversationId,
                "deleted", true
        );
    }

    public record ChatRequest(String message) {}
}