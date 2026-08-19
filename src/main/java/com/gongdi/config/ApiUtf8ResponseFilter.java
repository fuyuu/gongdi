package com.gongdi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * API 响应编码过滤器，显式声明 JSON 使用 UTF-8，降低中文响应在调试工具中乱码的概率。
 */
@Component
public class ApiUtf8ResponseFilter extends OncePerRequestFilter {

    /**
     * 对 /api 开头的接口统一设置 UTF-8 响应头。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/")) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
        }
        filterChain.doFilter(request, response);
    }
}
