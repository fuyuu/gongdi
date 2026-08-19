package com.gongdi.service;

import com.gongdi.domain.vo.WxSessionVO;

/**
 * 认证服务接口，定义小程序手机号登录能力。
 * @author Ma Qiang
 * @since 2026/8/13
 */
public interface IAuthService {


    WxSessionVO loginByPhone(String loginCode, String phone, String code);

    WxSessionVO loginByCodeId(String loginQuickCode);

    void sendPhoneCode(String phone);

    void verifyPhoneCode(String phone, String code);

}
