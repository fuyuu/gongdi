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
     * 注册登录拦截器，手机号登录接口与错误页放行
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/api/auth/wx-phone-login", "/api/auth/web-login", "/api/send-sms", "/api/wx-sms-login", "/error");
    }
}
