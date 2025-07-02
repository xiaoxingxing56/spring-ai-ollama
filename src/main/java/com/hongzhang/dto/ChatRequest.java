package com.hongzhang.dto;

import lombok.Data;

/**
 * 聊天请求DTO
 * 用于接收前端发送的聊天消息
 */
@Data
public class ChatRequest {
    
    /**
     * 用户输入的消息
     */
    private String message;
    
    /**
     * 对话ID，用于保持对话上下文
     */
    private String conversationId;
    
    /**
     * 构造函数
     */
    public ChatRequest() {}
    
    /**
     * 构造函数
     * @param message 用户消息
     * @param conversationId 对话ID
     */
    public ChatRequest(String message, String conversationId) {
        this.message = message;
        this.conversationId = conversationId;
    }
} 