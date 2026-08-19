package com.gongdi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置，显式提供 ObjectMapper 供微信响应解析与拦截器 JSON 输出使用。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Configuration
public class JacksonConfig {

    /**
     * 提供默认 ObjectMapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
