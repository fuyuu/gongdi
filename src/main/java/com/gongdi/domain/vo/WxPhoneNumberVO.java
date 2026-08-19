package com.gongdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信 getuserphonenumber 响应对象，承载手机号授权结果。
 * @author Ma Qiang
 * @since 2026/8/14
 */
@Data
public class WxPhoneNumberVO {

    /**
     * 错误码，0 表示成功
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 手机号信息
     */
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    /**
     * 微信返回的手机号详情。
     */
    @Data
    public static class PhoneInfo {

        /**
         * 带区号的手机号
         */
        private String phoneNumber;

        /**
         * 不带区号的纯手机号，业务绑定优先使用该字段
         */
        private String purePhoneNumber;

        /**
         * 国家区号
         */
        private String countryCode;
    }
}
