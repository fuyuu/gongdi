package com.gongdi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.yulichang.toolkit.StrUtils;
import com.gongdi.config.WechatProperties;
import com.gongdi.exception.ValidationException;
import com.gongdi.domain.entity.SysUser;
import com.gongdi.domain.vo.LoginVO;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.mapper.UserMapper;
import com.gongdi.service.IAuthService;
import com.gongdi.service.IWechatService;
import com.gongdi.util.JwtTokenUtils;
import com.gongdi.util.WechatUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类，负责小程序手机号登录、openid 绑定与 JWT 签发。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String EMAIL_CODE_PATTERN = "^\\d{6}$";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private RestTemplate restTemplate;

    private final UserMapper userMapper;
    private final WechatProperties wechatProperties;

    @Override
    public WxSessionVO loginByCodeId(String loginCode) {
        // 1. 调用微信接口兑换身份
        String wxUrl = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatProperties.getAppid(), wechatProperties.getSecret(), loginCode
        );
        String response = restTemplate.getForObject(wxUrl, String.class);
        JSONObject wxResult = JSON.parseObject(response);

        // 校验微信接口返回
        if (wxResult.containsKey("errcode")) {

        }

        String openid = wxResult.getString("openid");
        String sessionKey = wxResult.getString("session_key");
        String unionid = wxResult.getString("unionid");
        WxSessionVO userWx = wxResult.toJavaObject(WxSessionVO.class);

        // 2. 查询或注册用户
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            userMapper.insert(user);
        }

        // 3. 生成业务Token，缓存session_key
        String token = JwtUtil.generateToken(user.getId());
        // session_key 存入Redis，7天过期，用于后续解密手机号
        redisTemplate.opsForValue().set(
                "wx:session:" + user.getId(),
                sessionKey,
                7, TimeUnit.DAYS
        );

        // 4. 返回结果
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserInfo(user);
        return vo;
    }
    }

    @Override
    public void sendPhoneCode(String phone) {
        if(StrUtils.isBlank(phone)){
            log.info("手机号不能为空");
//            return WxSessionVO.builder().errmsg("手机号不能为空").build();
        }
    }

    @Override
    public void verifyPhoneCode(String phone, String code) {
        if(StrUtils.isBlank(phone)||StrUtils.isBlank(code)){
            log.info("手机号或验证码不能为空");
//            return WxSessionVO.builder().errmsg("手机号或验证码不能为空").build();
        }

    }
}
