package com.hongzhang.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * AI模型配置类
 * 使用Spring AI的自动配置功能
 */
@Slf4j
@Configuration
@Getter
public class ModelConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

    @Value("${spring.ai.ollama.chat.options.model}")
    private String modelName;

    @Value("${spring.ai.ollama.chat.options.temperature}")
    private Float temperature;

    /**
     * 初始化配置信息
     */
    @PostConstruct
    public void initConfig() {
        log.info("Spring AI Ollama配置初始化完成");
        log.info("Ollama服务地址: {}", baseUrl);
        log.info("使用模型: {}", modelName);
        log.info("温度参数: {}", temperature);
    }
}
