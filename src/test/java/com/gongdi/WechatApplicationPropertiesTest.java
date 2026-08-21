package com.gongdi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * 微信登录配置测试，防止生产默认值误停留在 mock 模式。
 */
class WechatApplicationPropertiesTest {

    @Test
    void applicationYmlUsesRealWechatLoginByDefault() throws Exception {
        Properties properties;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            assertThat("classpath 下应存在 application.yml", inputStream, notNullValue());
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new InputStreamResource(inputStream));
            properties = yaml.getObject();
        }

        // 微信登录默认必须走真实 jscode2session，mock 只能通过环境变量显式开启
        assertThat(properties.getProperty("wechat.mock-enabled"), is("${WECHAT_MOCK_ENABLED:false}"));
    }
}
