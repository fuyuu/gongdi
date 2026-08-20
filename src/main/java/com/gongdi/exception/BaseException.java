package com.gongdi.exception;

/**
 * 基础异常类，所有自定义异常都应继承自该类
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message,Object ... args) {
        super(String.format(message, args));
    }

    // 构造器二：带提示文字 + 底层原始异常（记录堆栈用）
    public BaseException(String message, Throwable cause) {
        super(message, cause); // 这里传给父类，可以保留原始错误的堆栈信息
    }

    // 构造器三（高级）：如果不需要具体消息，直接包一层原始异常
    public BaseException(Throwable cause) {
        super(cause);
    }

}
