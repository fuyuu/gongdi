package com.gongdi.service;

import com.gongdi.domain.dto.WxSmsLoginDTO;
import com.gongdi.domain.vo.LoginVO;
import com.gongdi.domain.vo.WxSessionVO;

/**
 * 认证服务接口，定义小程序登录能力。
 *
 * @author Ma Qiang
 * @since 2026/8/13
 */
public interface IAuthService {

    /**
     * 通过微信 loginCode 登录：换取 openid，老用户直接返回双 token，新用户自动注册后返回双 token。
     *
     * @param loginCode 小程序 wx.login 返回的临时 code
     * @return 登录结果（accessToken、refreshToken、是否新用户）
     */
    LoginVO loginByCodeId(String loginCode);

    /**
     * 刷新访问令牌：校验 refreshToken 后签发新的双 token。
     *
     * @param refreshToken 旧的刷新令牌
     * @return 新的登录结果
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 用户登出：删除 Redis 中的刷新令牌，使登录态失效。
     *
     * @param userId 用户ID
     */
    void logout(String userId);

    /**
     * 小程序手机号登录（预留，暂未实现）。
     */
    LoginVO loginByPhone(String loginCode, String phone, String code);

    /**
     * 短信验证码登录：先校验用户输入的验证码与 Redis 中存储的是否一致，
     * 通过后再用 loginCode 走微信登录（换 openid、注册、绑定手机号），最后签发双 token。
     *
     * @param dto 登录请求（loginCode、phone、smsCode）
     * @return 登录结果（accessToken、refreshToken、是否新用户）
     */
    LoginVO smsLogin(WxSmsLoginDTO dto);

    /**
     * 发送手机验证码。
     */
    void sendPhoneCode(String phone);

    /**
     * 校验手机验证码。
     */
    void verifyPhoneCode(String phone, String code);

}
