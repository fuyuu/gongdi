package com.gongdi.service.impl;

/**
 * 短信发送器接口，隔离外部短信网关，便于测试验证码业务逻辑。
 */
public interface SmsCodeSender {

    /**
     * 发送短信验证码。
     *
     * @param phone 接收短信的手机号
     * @param code 6 位数字验证码
     * @param validMinutes 验证码有效分钟数
     */
    void send(String phone, String code, int validMinutes);
}
