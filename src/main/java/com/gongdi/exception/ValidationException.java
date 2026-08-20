package com.gongdi.exception;

/**
 * 验证异常类，用于表示验证失败的异常。
 * 用于手动参数校验失败，比如「参数为空」「参数格式错误」。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
