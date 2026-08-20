package com.gongdi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongdi.config.WechatProperties;
import com.gongdi.constant.ResultCode;
import com.gongdi.exception.BusinessException;
import com.gongdi.exception.SystemException;
import com.gongdi.domain.vo.WxAccessTokenVO;
import com.gongdi.domain.vo.WxPhoneNumberVO;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.service.IWechatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 微信小程序服务实现，负责 code2session 调用。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatServiceImpl implements IWechatService {

    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
    private static final long ACCESS_TOKEN_SAFE_WINDOW_MILLIS = 300_000L;

    private final WechatProperties wechatProperties;
    private final ObjectMapper objectMapper;
    private volatile String cachedAccessToken;
    private volatile long accessTokenExpiresAt;

    /**
     * 调用微信 jscode2session 换取 openid，mock 模式下跳过联网。
     */
    @Override
    public WxSessionVO code2session(String code) {
        checkWechatConfig();

        // https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String url = CODE2SESSION_URL + "?appid=" + wechatProperties.getAppid()
                + "&secret=" + wechatProperties.getSecret()
                + "&js_code=" + encode(code)
                + "&grant_type=authorization_code";

        String body = doGet(url, "微信 jscode2session");

        WxSessionVO session;
        try {
            session = objectMapper.readValue(body, WxSessionVO.class);
        } catch (Exception e) {
            log.error("微信 jscode2session 响应解析失败: {}", body);
            throw new SystemException("微信登录响应解析失败");
        }

        if (session == null || !StringUtils.hasText(session.getOpenid())) {
            String errmsg = session != null && session.getErrmsg() != null ? session.getErrmsg() : "未知错误";
            log.warn("微信登录失败: {}", errmsg);
            throw new BusinessException(ResultCode.WECHAT_LOGIN_ERROR.getMsg() + "：" + errmsg);
        }
        return session;
    }

    /**
     * 用微信手机号授权 code 换取手机号，mock 模式下按 code 生成稳定手机号。
     */
    @Override
    public String getPhoneNumber(String phoneCode) {
        if (!StringUtils.hasText(phoneCode)) {
            throw new BusinessException("请先授权获取手机号");
        }

        // 本地联调时可直接传 11 位手机号，也可传任意 code 生成稳定手机号。
        if (wechatProperties.isMockEnabled()) {
            return mockPhoneNumber(phoneCode);
        }

        String accessToken = getAccessToken();

        // https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN
        String url = PHONE_NUMBER_URL + "?access_token=" + encode(accessToken);
        String body;
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of("code", phoneCode));
            body = doPostJson(url, requestBody, "微信 getuserphonenumber");
        } catch (Exception e) {
            log.error("微信手机号请求构造失败: {}", e.getMessage(), e);
            throw new SystemException("微信手机号接口调用失败");
        }

        WxPhoneNumberVO phoneNumber;
        try {
            phoneNumber = objectMapper.readValue(body, WxPhoneNumberVO.class);
        } catch (Exception e) {
            log.error("微信手机号响应解析失败: {}", body);
            throw new SystemException("微信手机号响应解析失败");
        }

        if (phoneNumber == null || phoneNumber.getErrcode() == null || phoneNumber.getErrcode() != 0) {
            String errmsg = phoneNumber != null && phoneNumber.getErrmsg() != null ? phoneNumber.getErrmsg() : "未知错误";
            log.warn("微信手机号授权失败: {}", errmsg);
            throw new BusinessException(ResultCode.WECHAT_LOGIN_ERROR.getMsg() + "：" + errmsg);
        }

        WxPhoneNumberVO.PhoneInfo phoneInfo = phoneNumber.getPhoneInfo();
        if (phoneInfo == null || !StringUtils.hasText(phoneInfo.getPurePhoneNumber())) {
            throw new BusinessException("微信未返回可绑定手机号");
        }
        return phoneInfo.getPurePhoneNumber();
    }

    /**
     * 解密旧版手机号授权密文，兼容仅返回 encryptedData/iv 的微信基础库或开发者工具。
     */
    @Override
    public String decryptPhoneNumber(String encryptedData, String iv, String sessionKey) {
        if (!StringUtils.hasText(encryptedData) || !StringUtils.hasText(iv)) {
            throw new BusinessException("请先授权获取手机号");
        }

        // mock 模式下允许测试直接传手机号，便于本地联调不依赖微信真实解密链路。
        if (wechatProperties.isMockEnabled()) {
            return mockPhoneNumber(encryptedData);
        }

        if (!StringUtils.hasText(sessionKey)) {
            throw new BusinessException("微信会话已过期，请重新授权手机号");
        }

        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(ivBytes));
            String json = new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
            WxPhoneNumberVO.PhoneInfo phoneInfo = objectMapper.readValue(json, WxPhoneNumberVO.PhoneInfo.class);
            if (phoneInfo == null || !StringUtils.hasText(phoneInfo.getPurePhoneNumber())) {
                throw new BusinessException("微信未返回可绑定手机号");
            }
            return phoneInfo.getPurePhoneNumber();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.warn("微信旧版手机号密文解密失败: {}", e.getMessage());
            throw new BusinessException("手机号授权失败，请重新点击登录授权");
        }
    }

    /**
     * 获取并缓存微信 access_token，避免每次手机号登录都重复请求微信接口。
     */
    private synchronized String getAccessToken() {
        long now = System.currentTimeMillis();
        if (StringUtils.hasText(cachedAccessToken) && now < accessTokenExpiresAt) {
            return cachedAccessToken;
        }

        checkWechatConfig();

        // https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String url = ACCESS_TOKEN_URL + "?grant_type=client_credential"
                + "&appid=" + encode(wechatProperties.getAppid())
                + "&secret=" + encode(wechatProperties.getSecret());
        String body = doGet(url, "微信 access_token");

        WxAccessTokenVO token;
        try {
            token = objectMapper.readValue(body, WxAccessTokenVO.class);
        } catch (Exception e) {
            log.error("微信 access_token 响应解析失败: {}", body);
            throw new SystemException("微信 access_token 响应解析失败");
        }

        if (token == null || !StringUtils.hasText(token.getAccessToken())) {
            String errmsg = token != null && token.getErrmsg() != null ? token.getErrmsg() : "未知错误";
            log.warn("微信 access_token 获取失败: {}", errmsg);
            throw new BusinessException(ResultCode.WECHAT_LOGIN_ERROR.getMsg() + "：" + errmsg);
        }

        int expiresIn = token.getExpiresIn() != null ? token.getExpiresIn() : 7200;
        cachedAccessToken = token.getAccessToken();
        accessTokenExpiresAt = now + expiresIn * 1000L - ACCESS_TOKEN_SAFE_WINDOW_MILLIS;
        return cachedAccessToken;
    }

    /**
     * 校验微信真实接口所需配置，避免空配置请求外部接口。
     */
    private void checkWechatConfig() {
        if (!StringUtils.hasText(wechatProperties.getAppid()) || !StringUtils.hasText(wechatProperties.getSecret())) {
            throw new SystemException("微信小程序 appid/secret 未配置");
        }
    }

    /**
     * 发起微信 GET 请求，并统一处理网络异常。
     */
    private String doGet(String url, String apiName) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            log.error("{} 调用失败: {}", apiName, e.getMessage(), e);
            throw new SystemException(apiName + "接口调用失败");
        }
    }

    /**
     * 发起微信 JSON POST 请求，并统一处理网络异常。
     */
    private String doPostJson(String url, String requestBody, String apiName) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            log.error("{} 调用失败: {}", apiName, e.getMessage(), e);
            throw new SystemException(apiName + "接口调用失败");
        }
    }

    /**
     * URL 编码微信接口参数，避免 code 或 secret 中特殊字符破坏请求。
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * mock 模式手机号规则：11 位手机号原样返回，否则根据 code 生成稳定测试手机号。
     */
    private String mockPhoneNumber(String phoneCode) {
        if (phoneCode.matches("^1\\d{10}$")) {
            return phoneCode;
        }
        long hash = Math.abs((long) phoneCode.hashCode());
        return "13" + String.format("%09d", hash % 1_000_000_000L);
    }
}
