package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应对象，返回 access token 和当前用户信息。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌，前端可在 accessToken 过期后用于换取新的访问令牌。
     */
    private String refreshToken;

    /**
     * 是否新用户（首次登录注册），用于前端判断是否需要引导填写资料
     */
    private Boolean isNewUser;
}
