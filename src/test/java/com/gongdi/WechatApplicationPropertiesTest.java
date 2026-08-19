package com.gongdi;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 微信登录配置测试，防止生产默认值误停留在 mock 模式。
 */
class WechatApplicationPropertiesTest {

    @Test
    void applicationPropertiesUsesRealWechatLoginByDefault() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        }

        // 真实小程序 appid 可以进入普通配置，appsecret 只允许从环境变量或本地私有配置读取。
        assertThat(properties.getProperty("spring.config.import"), is("optional:file:./application-local.properties"));
        assertThat(properties.getProperty("wechat.appid"), is("${WECHAT_APPID:wx44e6d6dfa6d18250}"));
        assertThat(properties.getProperty("wechat.secret"), is("${WECHAT_SECRET:}"));
        assertThat(properties.getProperty("wechat.mock-enabled"), is("${WECHAT_MOCK_ENABLED:false}"));
    }
}
