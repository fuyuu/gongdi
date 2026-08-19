package com.gongdi.service;

import com.gongdi.domain.vo.WxSessionVO;

/**
 * 微信小程序服务接口，定义 code 换会话能力。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public interface IWechatService {

    /**
     * 调用微信 jscode2session，用登录 code 换取 openid 与 session_key
     * @param code 小程序 wx.login 返回的临时登录凭证
     * @return 微信会话信息
     */
    WxSessionVO code2session(String code);

    /**
     * 调用微信 getuserphonenumber，用手机号授权 code 换取用户手机号
     * @param phoneCode 小程序 getPhoneNumber 返回的手机号动态凭证
     * @return 不带区号的纯手机号
     */
    String getPhoneNumber(String phoneCode);

    /**
     * 解密旧版 getPhoneNumber 返回的手机号密文，兼容未返回手机号动态 code 的微信基础库。
     * @param encryptedData 小程序端 getPhoneNumber 返回的 encryptedData
     * @param iv 小程序端 getPhoneNumber 返回的 iv
     * @param sessionKey wx.login code2session 换取的 session_key
     * @return 不带区号的纯手机号
     */
    String decryptPhoneNumber(String encryptedData, String iv, String sessionKey);
}
