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
}
