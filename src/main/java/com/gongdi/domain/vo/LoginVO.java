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
     * 令牌类型
     */
    private String tokenType;

    /**
     * 当前用户信息
     */
    private UserVO user;

    /**
     * 兼容早期只返回 accessToken 的测试和调用点，新登录链路优先使用四参构造。
     */
    public LoginVO(String accessToken, String tokenType, UserVO user) {
        this(accessToken, null, tokenType, user);
    }
}
