package com.gongdi.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gongdi.config.WechatProperties;
import com.gongdi.domain.vo.WxSessionVO;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class WechatUtil {
    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private static final long ACCESS_TOKEN_SAFE_WINDOW_MILLIS = 300_000L;

    private final WechatProperties wechatProperties;
    @Resource
    private RestTemplate restTemplate;

    public WxSessionVO code2session(String code) {
        String url = CODE2SESSION_URL + "?appid=" + wechatProperties.getAppid()
                + "&secret=" + wechatProperties.getSecret()
                + "&js_code=" + encode(code)
                + "&grant_type=authorization_code";

        // 2. 调用微信接口换取openid和session_key
        String response = restTemplate.getForObject(url, String.class);
        JSONObject sessionData = JSON.parseObject(response);
        String openid = sessionData.getString("openid");
        String sessionKey = sessionData.getString("session_key");

        return new WxSessionVO(openid,sessionKey);
    }

    /**
     * URL 编码微信接口参数，避免 code 或 secret 中特殊字符破坏请求。
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
