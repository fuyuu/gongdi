package com.gongdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信 access_token 响应对象，用于后端换取手机号前获取接口调用凭证。
 * @author Ma Qiang
 * @since 2026/8/14
 */
@Data
public class WxAccessTokenVO {

    /**
     * 接口调用凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token 有效期，单位秒
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * 错误码，成功时无此字段
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
