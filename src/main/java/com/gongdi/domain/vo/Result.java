package com.gongdi.domain.vo;

import com.gongdi.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应对象，小程序端统一按 code/message/data 解析。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    /**
     * 成功响应
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, null, data);
    }

    /**
     * 成功响应（自定义提示）
     * @param msg 自定义成功信息
     * @return 成功响应对象
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>(0, msg, null);
    }

    /**
     * 成功响应（数据 + 自定义提示）
     * @param data 响应数据
     * @param msg 自定义成功信息
     * @return 成功响应对象
     */
    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(0, msg, data);
    }

    /**
     * 失败响应
     * @param resultCode 结果码枚举
     * @return 失败响应对象
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     * 失败响应（自定义错误信息）
     * @param resultCode 结果码枚举
     * @param msg 自定义错误信息
     * @return 失败响应对象
     */
    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result<>(resultCode.getCode(), msg, null);
    }
}
