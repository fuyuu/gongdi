package com.gongdi.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongdi.constant.ResultCode;
import com.gongdi.domain.vo.Result;
import com.gongdi.util.JwtTokenUtils;
import com.gongdi.util.ThreadLocalUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 登录拦截器，校验 Authorization 请求头中的 JWT 并解析当前用户。
 * 与 smartorder 一致，鉴权由拦截器承担（不依赖 Spring Security）。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    /**
     * 预处理回调，在控制器处理请求之前校验登录态
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        // 兼容 SSE / 无法携带请求头场景，支持 ?token= 参数
        if (authorization == null || authorization.trim().isEmpty()) {
            String urlToken = request.getParameter("token");
            if (urlToken != null && !urlToken.trim().isEmpty()) {
                authorization = "Bearer " + urlToken;
            }
        }
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new RuntimeException("无效的Token");
            }
            String token = authorization.substring(7).trim();
            Claims claims = JwtTokenUtils.getClaimsFromToken(token, JwtTokenUtils.ACCESS_TOKEN_SECRET);
            ThreadLocalUtils.set(claims);
            return true;
        } catch (Exception e) {
            log.error("JWT令牌解析失败---未登录: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(ResultCode.UNAUTHORIZED)));
            return false;
        }
    }

    /**
     * 请求完成后清理线程本地变量，避免线程复用导致用户串号
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalUtils.remove();
    }
}
