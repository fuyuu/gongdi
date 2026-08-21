package com.gongdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信 jscode2session 响应对象
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxSessionVO {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;

    /**
     * 用户在微信开放平台的唯一标识（可选返回）
     * 仅当小程序绑定微信开放平台账号后，才会返回该字段
     */
    private String unionid;

    /**
     * 错误码（登录失败时返回）
     */
    private Integer errcode;

    /**
     * 错误信息（登录失败时返回）
     */
    private String errmsg;
}
