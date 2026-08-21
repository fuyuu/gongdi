package com.gongdi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gongdi.config.SmsProperties;
import com.gongdi.exception.BusinessException;
import com.gongdi.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.github.yulichang.toolkit.StrUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Spug 短信网关发送器，通过 GET 请求调用 Spug 短信模板接口发送验证码。
 *
 * <p>API 文档：<a href="https://push.spug.cc/guide/sms">https://push.spug.cc/guide/sms</a></p>
 *
 * <pre>{@code
 * GET https://push.spug.cc/sms/{templateCode}?to=13800000000&code=123456&number=10
 * }</pre>
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpugSmsCodeSender implements SmsCodeSender {

    private final RestTemplate restTemplate;
    private final SmsProperties smsProperties;

    /**
     * 发送短信验证码。
     *
     * @param phone        接收短信的手机号
     * @param code         验证码（4-6 位数字或字母）
     * @param validMinutes 验证码有效分钟数
     */
    @Override
    public void send(String phone, String code, int validMinutes) {
        // 参数校验
        if (StrUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }
        if (StrUtils.isBlank(code)) {
            throw new BusinessException("验证码不能为空");
        }
        if (validMinutes <= 0) {
            throw new BusinessException("验证码有效期必须大于 0");
        }

        // mock 模式：只打日志，不真实发送
        if (smsProperties.isMockEnabled()) {
            log.info("[短信mock] 验证码已生成，手机号: {}, 验证码: {}, 有效期: {} 分钟", phone, code, validMinutes);
            return;
        }

        // 构建请求 URL
        String templateUrl = smsProperties.getBaseUrl() + "/" + smsProperties.getTemplateCode();
        URI uri = UriComponentsBuilder.fromUriString(templateUrl)
                .queryParam("to", phone)
                .queryParam("code", code)
                .queryParam("number", String.valueOf(validMinutes))
                .build()
                .toUri();

        log.debug("发送短信验证码, phone: {}, uri: {}", phone, uri);

        // 发起 GET 请求
        String responseBody;
        try {
            responseBody = restTemplate.getForObject(uri, String.class);
        } catch (RestClientException e) {
            log.error("短信网关调用失败, phone: {}, error: {}", phone, e.getMessage(), e);
            throw new SystemException("短信验证码发送失败，请稍后重试");
        }

        // 解析响应
        if (StrUtils.isBlank(responseBody)) {
            log.error("短信网关返回空响应, phone: {}", phone);
            throw new SystemException("短信验证码发送失败，请稍后重试");
        }

        JSONObject result = JSON.parseObject(responseBody);
        Integer respCode = result.getInteger("code");
        if (respCode == null || respCode != 200) {
            String msg = result.getString("msg");
            if (StrUtils.isBlank(msg)) {
                msg = "未知错误";
            }
            log.error("短信网关返回错误, phone: {}, code: {}, msg: {}", phone, respCode, msg);
            throw new SystemException("短信验证码发送失败：" + msg);
        }

        log.info("短信验证码发送成功, phone: {}, request_id: {}", phone, result.getString("request_id"));
    }
}