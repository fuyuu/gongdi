package com.gongdi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信验证码配置，集中管理 Spug 短信模板、验证码有效期和本地 mock 开关。
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * Spug 短信模板地址，发送器会在后面追加 to、code、number 参数。
     */
    private String templateUrl = "https://push.spug.cc/sms/WwdMZaJrSjS1Y1JjIDST3w";

    /**
     * 验证码有效分钟数，对应短信模板中的 number 参数。
     */
    private int codeTtlMinutes = 15;

    /**
     * 同一手机号重复发送验证码的最小间隔秒数，防止用户连续点击。
     */
    private int resendIntervalSeconds = 60;

    /**
     * 本地 mock 开关，开启后只生成并保存验证码，不真实调用短信网关。
     */
    private boolean mockEnabled = false;

    /**
     * 固定验证码，主要用于集成测试或本地联调；为空时随机生成 6 位数字。
     */
    private String fixedCode;
}
