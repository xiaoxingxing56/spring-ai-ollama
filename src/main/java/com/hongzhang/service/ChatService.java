package com.hongzhang.service;

import com.hongzhang.config.ModelConfig;
import com.hongzhang.dto.ChatRequest;
import com.hongzhang.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import jakarta.annotation.PostConstruct;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天服务类
 * 处理与AI模型的交互逻辑
 */
@Slf4j
@Service
public class ChatService {

    @Autowired
    private ModelConfig modelConfig;

    private WebClient webClient;
    
    /**
     * 存储对话历史的内存缓存
     * 在实际生产环境中应该使用Redis等持久化存储
     */
    private final ConcurrentHashMap<String, String> conversationHistory = new ConcurrentHashMap<>();

    /**
     * 初始化WebClient
     * 使用@PostConstruct确保在依赖注入完成后执行
     */
    @PostConstruct
    public void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(modelConfig.getBaseUrl())
                .build();
        log.info("WebClient初始化完成，基础URL: {}", modelConfig.getBaseUrl());
    }

    /**
     * 发送聊天消息
     * @param request 聊天请求
     * @return 聊天响应
     */
    public Mono<ChatResponse> sendMessage(ChatRequest request) {
        log.info("收到聊天请求: {}", request.getMessage());
        
        try {
            // 生成或获取对话ID
            final String conversationId = request.getConversationId() != null && !request.getConversationId().isEmpty() ? 
                request.getConversationId() : UUID.randomUUID().toString();
            
            if (request.getConversationId() == null || request.getConversationId().isEmpty()) {
                log.info("生成新的对话ID: {}", conversationId);
            }
            
            // 获取对话历史
            String history = conversationHistory.getOrDefault(conversationId, "");
            
            // 构建完整的消息（包含历史上下文）
            String fullMessage = history.isEmpty() ? 
                request.getMessage() : 
                history + "\n\n用户: " + request.getMessage();
            
            // 构建Ollama API请求
            OllamaRequest ollamaRequest = new OllamaRequest();
            ollamaRequest.setModel(modelConfig.getModelName());
            ollamaRequest.setPrompt(fullMessage);
            ollamaRequest.setStream(false);
            ollamaRequest.setOptions(new OllamaOptions(modelConfig.getTemperature()));
            
            // 调用Ollama API
            return webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ollamaRequest)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .map(response -> {
                        // 更新对话历史
                        String newHistory = fullMessage + "\n\nAI: " + response.getResponse();
                        // 限制历史长度，避免内存溢出
                        if (newHistory.length() > 5000) {
                            newHistory = newHistory.substring(newHistory.length() - 5000);
                        }
                        conversationHistory.put(conversationId, newHistory);
                        
                        log.info("AI回复成功，对话ID: {}", conversationId);
                        return ChatResponse.success(response.getResponse(), conversationId);
                    })
                    .onErrorResume(error -> {
                        log.error("AI调用失败: {}", error.getMessage(), error);
                        return Mono.just(ChatResponse.error("AI服务暂时不可用，请稍后重试", conversationId));
                    });
                    
        } catch (Exception e) {
            log.error("处理聊天请求时发生错误: {}", e.getMessage(), e);
            return Mono.just(ChatResponse.error("服务器内部错误", request.getConversationId()));
        }
    }
    
    /**
     * 清除指定对话的历史记录
     * @param conversationId 对话ID
     * @return 操作结果
     */
    public Mono<ChatResponse> clearConversation(String conversationId) {
        log.info("清除对话历史: {}", conversationId);
        
        if (conversationId != null && !conversationId.isEmpty()) {
            conversationHistory.remove(conversationId);
        }
        
        return Mono.just(ChatResponse.success("对话历史已清除", conversationId));
    }
    
    /**
     * 获取对话历史数量（用于监控）
     * @return 当前活跃对话数量
     */
    public int getActiveConversationCount() {
        return conversationHistory.size();
    }
    
    // Ollama API请求和响应的内部类
    private static class OllamaRequest {
        private String model;
        private String prompt;
        private boolean stream;
        private OllamaOptions options;
        
        // Getters and Setters
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
        public boolean isStream() { return stream; }
        public void setStream(boolean stream) { this.stream = stream; }
        public OllamaOptions getOptions() { return options; }
        public void setOptions(OllamaOptions options) { this.options = options; }
    }
    
    private static class OllamaOptions {
        private float temperature;
        
        public OllamaOptions(float temperature) {
            this.temperature = temperature;
        }
        
        public float getTemperature() { return temperature; }
        public void setTemperature(float temperature) { this.temperature = temperature; }
    }
    
    private static class OllamaResponse {
        private String response;
        
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
    }
} 