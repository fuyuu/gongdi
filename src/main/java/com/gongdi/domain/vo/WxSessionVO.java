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
     * 用户在开放平台的唯一标识
     */
    private String unionid;

    /**
     * 错误码，成功时无此字段
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
