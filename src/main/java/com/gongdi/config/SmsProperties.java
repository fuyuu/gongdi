package com.gongdi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信验证码配置，集中管理 Spug 短信模板、验证码有效期和本地 mock 开关。
 *
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * Spug 短信网关基础地址，发送时拼接 /{templateCode}。
     */
    private String baseUrl = "https://push.spug.cc/sms";

    /**
     * Spug 短信模板编码，作为 URL 路径参数，由业务配置决定。
     */
    private String templateCode = "WwdMZaJrSjS1Y1JjIDST3w";

    /**
     * 验证码有效分钟数，对应短信模板中的 number 参数。
     */
    private int codeTtlMinutes = 15;

    /**
     * 同一手机号重复发送验证码的最小间隔秒数，防止用户连续点击。
     */
    private int resendIntervalSeconds = 60;

    /**
     * 验证码长度，默认 6 位数字。
     */
    private int codeLength = 6;

    /**
     * 本地 mock 开关，开启后只生成并保存验证码，不真实调用短信网关。
     */
    private boolean mockEnabled = false;

    /**
     * 固定验证码，主要用于集成测试或本地联调；为空时随机生成 6 位数字。
     */
    private String fixedCode;
}
