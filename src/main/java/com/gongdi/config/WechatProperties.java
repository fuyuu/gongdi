package com.gongdi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置项
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    /**
     * 小程序 appid
     */
    private String appid;

    /**
     * 小程序 appsecret
     */
    private String secret;

    /**
     * 是否开启 mock 模式（跳过 jscode2session 联网，按 code 派生 openid）
     */
    private boolean mockEnabled = false;
}
