package com.gongdi.exception;

/**
 * 业务异常类，用于表示业务级别的异常。
 * 用于处理「预期内」的逻辑阻断，比如「余额不足」「用户已存在」。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object... args) {
        super(String.format(message, args));
    }
}
