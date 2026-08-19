package com.gongdi.constant;

import lombok.Getter;

/**
 * 统一的结果码枚举类
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Getter
public enum ResultCode {

    BUSINESS_ERROR(400, "业务异常"),
    VALIDATION_ERROR(400, "参数校验失败"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统异常"),
    WECHAT_LOGIN_ERROR(4001, "微信登录失败");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
