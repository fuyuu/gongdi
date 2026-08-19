package com.gongdi.util;

import com.gongdi.constant.ResultCode;
import com.gongdi.exception.BusinessException;
import io.jsonwebtoken.Claims;

/**
 * 线程本地工具类，保存当前请求的 JWT claims，供控制器取当前用户。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class ThreadLocalUtils {

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    public static void set(Object value) {
        THREAD_LOCAL.set(value);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前登录用户 ID，未登录时抛出未授权异常
     * @return 当前用户 ID
     */
    public static Long getCurrentUserId() {
        Claims claims = ThreadLocalUtils.get();
        if (claims == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getMsg());
        }
        Object userId = claims.get("userId");
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getMsg());
        }
        return ((Number) userId).longValue();
    }
}
