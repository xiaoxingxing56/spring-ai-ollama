package com.hongzhang.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 聊天响应DTO
 * 用于返回AI的回复消息
 */
@Data
public class ChatResponse {
    
    /**
     * AI回复的消息
     */
    private String response;
    
    /**
     * 对话ID
     */
    private String conversationId;
    
    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
    
    /**
     * 构造函数
     */
    public ChatResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * 构造函数
     * @param response AI回复
     * @param conversationId 对话ID
     */
    public ChatResponse(String response, String conversationId) {
        this();
        this.response = response;
        this.conversationId = conversationId;
        this.success = true;
    }
    
    /**
     * 创建成功响应
     * @param response AI回复
     * @param conversationId 对话ID
     * @return ChatResponse实例
     */
    public static ChatResponse success(String response, String conversationId) {
        return new ChatResponse(response, conversationId);
    }
    
    /**
     * 创建错误响应
     * @param errorMessage 错误信息
     * @param conversationId 对话ID
     * @return ChatResponse实例
     */
    public static ChatResponse error(String errorMessage, String conversationId) {
        ChatResponse response = new ChatResponse();
        response.setErrorMessage(errorMessage);
        response.setConversationId(conversationId);
        response.setSuccess(false);
        return response;
    }
} 