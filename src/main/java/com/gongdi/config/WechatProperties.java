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

}
