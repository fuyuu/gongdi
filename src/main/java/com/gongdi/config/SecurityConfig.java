package com.gongdi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security 配置类。
 * <p>
 * 本系统鉴权由 {@link com.gongdi.interceptor.LoginInterceptor} 承担，不依赖 Spring Security。
 * 这里仅关闭默认的 CSRF 与登录拦截，放行所有请求，避免 Spring Security 在拦截器之前提前拦截。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Configuration
public class SecurityConfig {

    /**
     * 配置安全过滤链：放行所有请求，鉴权逻辑由 LoginInterceptor 处理。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        // 放行所有请求，鉴权由 LoginInterceptor 处理
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(
                        org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }
}
