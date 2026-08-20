package com.gongdi.exception;

import lombok.Getter;

/**
 * 基础异常类，所有自定义异常都应继承自该类
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Getter
public class AppExceptions extends RuntimeException {
    
    private static final long serialVersionUID = 5317680961212299217L;
    
    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误信息
     */
    private final String msg;
    
    /**
     * 只传错误信息，默认错误码 500
     */
    public AppExceptions(String msg) {
        super(msg);
        this.code = 500;
        this.msg = msg;
    }
    /**
     * 传错误码 + 错误信息（最常用）
     */
    public AppExceptions(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    /**
     * 传错误码 + 错误信息 + 原始异常（包装底层异常时用）
     */
    public AppExceptions(Integer code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }
}
