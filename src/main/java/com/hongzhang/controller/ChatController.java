package com.hongzhang.controller;

import com.hongzhang.dto.ChatRequest;
import com.hongzhang.dto.ChatResponse;
import com.hongzhang.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 聊天控制器
 * 处理聊天相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // 允许跨域请求，用于前端开发
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 发送聊天消息
     * @param request 聊天请求
     * @return 聊天响应
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        log.info("收到聊天请求: {}", request.getMessage());
        
        // 验证请求参数
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return Mono.just(ChatResponse.error("消息内容不能为空", request.getConversationId()));
        }
        
        return chatService.sendMessage(request);
    }

    /**
     * 清除对话历史
     * @param conversationId 对话ID
     * @return 操作结果
     */
    @PostMapping(value = "/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ChatResponse> clearConversation(@RequestParam(required = false) String conversationId) {
        log.info("清除对话历史请求，对话ID: {}", conversationId);
        return chatService.clearConversation(conversationId);
    }

    /**
     * 获取系统状态信息
     * @return 系统状态
     */
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> getStatus() {
        return Mono.just(new Object() {
            public final String status = "running";
            public final int activeConversations = chatService.getActiveConversationCount();
            public final String timestamp = java.time.LocalDateTime.now().toString();
        });
    }

    /**
     * 健康检查接口
     * @return 健康状态
     */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> health() {
        return Mono.just(new Object() {
            public final String status = "UP";
            public final String service = "Spring AI Ollama Chat";
            public final String timestamp = java.time.LocalDateTime.now().toString();
        });
    }
} 