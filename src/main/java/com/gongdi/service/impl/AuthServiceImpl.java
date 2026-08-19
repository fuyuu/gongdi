package com.gongdi.service.impl;

import com.github.yulichang.toolkit.StrUtils;
import com.gongdi.exception.ValidationException;
import com.gongdi.domain.entity.SysUser;
import com.gongdi.domain.vo.LoginVO;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.mapper.UserMapper;
import com.gongdi.service.IAuthService;
import com.gongdi.service.IWechatService;
import com.gongdi.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private final IWechatService wechatService;
    private final UserMapper userMapper;
    private final IUserService userService;


    /**
     * 根据微信登录 code 和手机号授权凭证登录；优先使用新版 phoneCode，旧版 encryptedData/iv 作为兼容兜底。
     */
    @Override
    public LoginVO loginByWxPhone(String loginCode, String phoneCode, String encryptedData, String iv) {
        if (!StringUtils.hasText(loginCode)) {
            throw new ValidationException("微信登录 code 不能为空");
        }
        boolean hasPhoneCode = StringUtils.hasText(phoneCode);
        boolean hasEncryptedPhonePayload = StringUtils.hasText(encryptedData) && StringUtils.hasText(iv);
        if (!hasPhoneCode && !hasEncryptedPhonePayload) {
            throw new ValidationException("手机号授权 code 不能为空");
        }

        // wx.login code 只负责换取 openid，不直接作为业务账号。
        WxSessionVO session = wechatService.code2session(loginCode);
        String openid = session.getOpenid();

        // 新版基础库使用 phoneCode 换手机号；旧版基础库没有 phoneCode 时使用 session_key 解密手机号密文。
        String mobile = hasPhoneCode
                ? wechatService.getPhoneNumber(phoneCode)
                : wechatService.decryptPhoneNumber(encryptedData, iv, session.getSessionKey());
        SysUser user = bindOrCreateMobileUser(mobile, openid);
        checkUserStatus(user);

        // 签发 JWT（claims 携带 userId + role + openid），后续接口从 token 解析当前用户。
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("openid", openid);
        return buildLoginVO(user, claims);
    }

    /**
     * 按手机号查找或创建用户，并完成 openid 绑定。
     */
    /**
     * 根据微信登录 code 和已通过短信验证码校验的手机号登录，并完成手机号账号与 openid 的绑定。
     */
    @Override
    public LoginVO loginByWxSms(String loginCode, String phone) {
        if (!StringUtils.hasText(loginCode)) {
            throw new ValidationException("微信登录 code 不能为空");
        }
        String mobile = normalizeSmsLoginPhone(phone);

        // 短信验证码只证明手机号归属，仍通过 wx.login code 换取 openid 作为小程序身份。
        WxSessionVO session = wechatService.code2session(loginCode);
        String openid = session.getOpenid();
        SysSysUser user = bindOrCreateMobileUser(mobile, openid);
        checkUserStatus(user);

        // 短信登录复用与微信手机号登录一致的 JWT claims，保证后续接口读取当前用户方式一致。
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("openid", openid);
        return buildLoginVO(user, claims);
    }

    private SysUser bindOrCreateMobileUser(String mobile, String openid) {
        SysUser openidUser = userMapper.findByOpenid(openid);
        SysUser mobileUser = userMapper.findByMobile(mobile);

        if (mobileUser == null) {
            return bindMobileWhenOnlyOpenidExistsOrCreate(openidUser, mobile, openid);
        }

        if (openidUser != null && !mobileUser.getId().equals(openidUser.getId())) {
            throw new ValidationException("当前微信已绑定其他手机号，请联系管理员处理");
        }

        if (StringUtils.hasText(mobileUser.getOpenid()) && !openid.equals(mobileUser.getOpenid())) {
            throw new ValidationException("该手机号已绑定其他微信账号，请联系管理员处理");
        }

        if (!StringUtils.hasText(mobileUser.getOpenid())) {
            int updated = userMapper.bindOpenid(mobileUser.getId(), openid);
            if (updated == 0) {
                throw new ValidationException("该手机号已绑定其他微信账号，请联系管理员处理");
            }
            log.info("手机号账号绑定微信 openid，userId: {}, mobile: {}", mobileUser.getId(), mobile);
            return userMapper.findById(mobileUser.getId());
        }

        return mobileUser;
    }

    /**
     * 兼容早期只保存 openid 的临时账号；没有历史账号时创建新的手机号账号。
     */
    private SysUser bindMobileWhenOnlyOpenidExistsOrCreate(SysUser openidUser, String mobile, String openid) {
        if (openidUser != null) {
            if (StringUtils.hasText(openidUser.getPhone()) && !mobile.equals(openidUser.getPhone())) {
                throw new ValidationException("当前微信已绑定其他手机号，请联系管理员处理");
            }
            int updated = userMapper.bindMobile(openidUser.getId(), mobile);
            if (updated == 0) {
                throw new ValidationException("当前微信已绑定其他手机号，请联系管理员处理");
            }
            log.info("历史微信账号补绑定手机号，userId: {}, mobile: {}", openidUser.getId(), mobile);
            return userMapper.findById(openidUser.getId());
        }

        SysUser user = userMapper.insertByMobileAndOpenid(mobile, openid);
        log.info("手机号新用户入库并绑定微信，userId: {}, mobile: {}", user.getId(), mobile);
        return user;
    }

    /**
     * 登录前校验账号状态，停用账号不允许签发 token。
     */
    private void checkUserStatus(SysUser user) {
        if (user == null) {
            throw new ValidationException("用户登录失败，请联系管理员");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new ValidationException("账号已停用，请联系管理员");
        }
    }
    /**
     * 校验短信登录手机号格式，避免非法手机号进入用户绑定流程。
     */
    private String normalizeSmsLoginPhone(String phone) {
        String mobile = phone == null ? "" : phone.trim();
        if (!mobile.matches(PHONE_PATTERN)) {
            throw new ValidationException("请输入正确的手机号");
        }
        return mobile;
    }

    /**
     * 统一签发 accessToken 和 refreshToken，保持所有登录入口返回结构一致。
     */
    private LoginVO buildLoginVO(SysUser user, Map<String, Object> claims) {
        String accessToken = JwtTokenUtils.generateAccessToken(claims);
        String refreshToken = JwtTokenUtils.generateRefreshToken(claims);
        return new LoginVO(accessToken, refreshToken, "Bearer", userService.toUserVO(user));
    }

    @Override
    public WxSessionVO loginByPhone(String loginCode, String phone, String code) {
        if(StrUtils.isBlank(loginCode)){
            return WxSessionVO.builder().errmsg("登录loginCode不能为空,没有获得微信小程序的登录code").build();
        }
        if(StrUtils.isBlank(phone)||StrUtils.isBlank(code)){
            return WxSessionVO.builder().errmsg("手机号或验证码不能为空").build();
        }
        return null;
    }

    @Override
    public WxSessionVO loginByCodeId(String loginCode) {
        if(StrUtils.isBlank(loginCode)){
            return WxSessionVO.builder().errmsg("登录loginCode不能为空,没有获得微信小程序的登录code").build();
        }
        return null;
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
