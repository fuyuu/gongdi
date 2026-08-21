package com.gongdi.config;

import com.gongdi.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置，注册登录拦截器。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    /**
     * 注册登录拦截器，认证相关接口与错误页放行。
     * 所有 /api/auth/** 均为登录/刷新等公开接口，无需鉴权。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/auth/**", "/error");
    }
}
