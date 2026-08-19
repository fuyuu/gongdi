package com.gongdi.exception;

import com.gongdi.constant.ResultCode;
import com.gongdi.domain.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理类，统一转换为小程序可识别的 JSON 结构。
 * 说明：返回 HTTP 200 + body code，方便 wx.request 在 success 回调中按 code 判断成功/失败。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源未找到异常
     * @param e 资源未找到异常
     * @return 错误信息
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<String> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("资源未找到: {}", e.getResourcePath());
        return Result.fail(ResultCode.NOT_FOUND, e.getMessage());
    }

    /**
     * 处理验证异常
     * @param e 验证异常对象
     * @return 错误信息
     */
    @ExceptionHandler(ValidationException.class)
    public Result<String> handleValidationException(ValidationException e) {
        log.error("验证异常: {}", e.getMessage());
        return Result.fail(ResultCode.VALIDATION_ERROR, e.getMessage());
    }

    /**
     * 处理业务异常
     * @param e 业务异常对象
     * @return 错误信息
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(ResultCode.BUSINESS_ERROR, e.getMessage());
    }

    /**
     * 处理系统异常
     * @param e 系统异常对象
     * @return 错误信息
     */
    @ExceptionHandler(SystemException.class)
    public Result<String> handleSystemException(SystemException e) {
        log.error("系统异常: {}", e.getMessage());
        return Result.fail(ResultCode.SYSTEM_ERROR, e.getMessage());
    }

    /**
     * 处理所有未捕获的异常，避免把 Java 堆栈直接暴露给小程序端
     * @param e 抛出的异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("未处理的异常: {}", e.getMessage(), e);
        return Result.fail(ResultCode.SYSTEM_ERROR, e.getMessage());
    }
}
