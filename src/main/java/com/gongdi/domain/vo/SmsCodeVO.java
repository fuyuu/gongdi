package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码发送响应，给小程序端展示发送结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeVO {

    /**
     * 是否已受理短信验证码发送请求。
     */
    private Boolean success;

    /**
     * 前端可直接展示的提示文案。
     */
    private String message;
}
