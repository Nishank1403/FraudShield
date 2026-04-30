package com.fraudshield.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraudshield.dto.TransactionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

@Service
@ConditionalOnProperty(name = "fraudshield.queue.mode", havingValue = "redis", matchIfMissing = true)
public class TransactionQueueService implements QueuePublisher {
    
    // Using <String, String> based on your original implementation
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${fraudshield.queue.key}")
    private String queueKey;

    // Added @Qualifier here, while keeping both required dependencies
    public TransactionQueueService(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.findAndRegisterModules();
    }

    @Override
    public void enqueue(TransactionRequest request) {
        if (request.getTimestamp() == null) {
            request.setTimestamp(Instant.now());
        }
        try {
            String json = objectMapper.writeValueAsString(request);
            redisTemplate.opsForList().rightPush(queueKey, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize transaction", e);
        }
    }

    public TransactionRequest dequeue() {
        String json = redisTemplate.opsForList().leftPop(queueKey);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, TransactionRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize transaction", e);
        }
    }
}