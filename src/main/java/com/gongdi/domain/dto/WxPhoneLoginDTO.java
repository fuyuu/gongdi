package com.gongdi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信手机号登录请求对象，明确区分微信登录凭证和手机号授权凭证。
 * @author Ma Qiang
 * @since 2026/8/14
 */
@Data
@NoArgsConstructor
public class WxPhoneLoginDTO {

    /**
     * wx.login 返回的一次性登录凭证，只用于后端换取 openid 与 session_key。
     */
    //目前只使用了这个
    private String loginCode;

    /**
     * getPhoneNumber 返回的一次性手机号授权凭证，只用于后端换取 purePhoneNumber。
     */
    private String phoneCode;

    /**
     * 旧版 getPhoneNumber 返回的加密手机号数据，用于兼容没有 detail.code 的开发工具或基础库。
     */
    private String encryptedData;

    /**
     * 旧版 getPhoneNumber 返回的 AES 初始化向量，必须与 encryptedData 和 session_key 配套使用。
     */
    private String iv;
}
