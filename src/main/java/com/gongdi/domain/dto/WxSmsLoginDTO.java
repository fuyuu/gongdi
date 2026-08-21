package com.gongdi.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信短信验证码登录请求对象，字段名与小程序端契约保持一致。
 */
@Data
@AllArgsConstructor

public class WxSmsLoginDTO {

    /**
     * wx.login 返回的一次性登录凭证，用于后端换取 openid。
     */
    @JsonProperty("login_code")
    private String loginCode;

    /**
     * 用户主动输入并已接收短信验证码的手机号。
     */
    private String phone;

    /**
     * 用户输入的 6 位短信验证码。
     */
    @JsonProperty("sms_code")
    private String smsCode;
}
