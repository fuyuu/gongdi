package com.gongdi.controller;

import com.gongdi.domain.dto.RefreshTokenDTO;
import com.gongdi.domain.dto.WxLoginDTO;
import com.gongdi.domain.dto.WxSmsLoginDTO;
import com.gongdi.domain.vo.LoginVO;
import com.gongdi.domain.vo.Result;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.service.IAuthService;
import com.gongdi.util.ThreadLocalUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器，处理小程序登录请求。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    /**
     * 小程序快捷登录：logincode 换 openid，老用户返回双 token，新用户自动注册后返回双 token。
     */
    @PostMapping("/wx-id-login")
    public Result<LoginVO> wxIdLogin(@Valid @RequestBody WxLoginDTO dto) {
        LoginVO loginVO = authService.loginByCodeId(dto.getLoginCode());
        return Result.success(loginVO);
    }

    /**
     * 刷新访问令牌：用 refreshToken 换取新的双 token。
     */
    @PostMapping("/refresh-token")
    public Result<LoginVO> refreshToken(@Valid @RequestBody RefreshTokenDTO dto) {
        LoginVO loginVO = authService.refreshToken(dto.getRefreshToken());
        return Result.success(loginVO);
    }

    /**
     * 用户登出：删除 Redis 中的刷新令牌，使登录态失效。
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        Long userId = ThreadLocalUtils.getCurrentUserId();
        authService.logout(userId.toString());
        return Result.success("已成功退出登录");
    }

    /**
     * 短信验证码登录：先校验手机验证码与 Redis 中存储的是否一致，
     * 通过后用 loginCode 走微信登录并绑定手机号，返回双 token。
     */
    @PostMapping("/wx-sms-login")
    public Result<LoginVO> smsLogin(@RequestBody WxSmsLoginDTO dto) {
        LoginVO loginVO = authService.smsLogin(dto);
        return Result.success(loginVO);
    }

    /**
     * 小程序手机号登录：loginCode 换微信身份，phoneCode 换手机号，后端完成手机号账号绑定。
     */
    @PostMapping("/wx-phone-login")
    public Result<LoginVO> wxPhoneLogin(@RequestParam String loginCode,
                                            @RequestParam String phone,
                                            @RequestParam String code) {
        LoginVO wxSessionVO = authService.loginByPhone(loginCode, phone, code);
        return Result.success(wxSessionVO);
    }

    /**
     * 发送-手机验证码
     */
    @PostMapping("/send-phone-code")
    public Result<String> sendPhoneCode(@RequestParam String phone) {
        authService.sendPhoneCode(phone);
        return Result.success("验证码发送成功");
    }

    /**
     * 验证-手机验证码
     */
    @PostMapping("/verify-phone-code")
    public Result<String> verifyPhoneCode(@RequestParam String phone, @RequestParam String code) {
        authService.verifyPhoneCode(phone, code);
        return Result.success("验证码验证成功");
    }
}
