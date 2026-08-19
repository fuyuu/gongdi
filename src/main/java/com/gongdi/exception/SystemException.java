package com.gongdi.exception;

/**
 * 系统异常类，用于表示系统级别的异常。
 * 用于处理非预期的技术错误，如外部接口调用失败、文件读取失败。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public class SystemException extends BaseException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Object... args) {
        super(String.format(message, args));
    }
}
