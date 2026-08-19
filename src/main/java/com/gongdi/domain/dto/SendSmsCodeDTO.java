package com.gongdi.domain.dto;

import lombok.Data;

/**
 * 发送短信验证码请求对象，承接小程序端用户输入的手机号。
 */
@Data
public class SendSmsCodeDTO {

    /**
     * 接收短信验证码的手机号。
     */
    private String phone;
}
