package com.gongdi.controller;

import com.gongdi.domain.vo.Result;
import com.gongdi.domain.vo.WxSessionVO;
import com.gongdi.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器，处理小程序唯一的手机号登录请求。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    /**
     * 小程序手机号登录：loginCode 换微信身份，phoneCode 换手机号，后端完成手机号账号绑定。
     */
    @PostMapping("/wx-phone-login")
    public Result<WxSessionVO> wxPhoneLogin(@RequestParam String LoginCode, @RequestParam String phone,@RequestParam String code) {
        // 请求体为空时交给 service 做参数校验，避免控制器抛出空指针异常。
        WxSessionVO wxSessionVO = authService.loginByPhone(LoginCode, phone, code);
        return Result.success(wxSessionVO);
    }

    /**
     * 小程序快捷登录
     */
    @PostMapping("/wx-id-login")
    public Result<WxSessionVO> webLogin(@RequestParam String LoginCode) {
        WxSessionVO wxSessionVO = authService.loginByCodeId(LoginCode);
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
    public Result<String> verifyPhoneCode(@RequestParam String phone,@RequestParam String code) {
        authService.verifyPhoneCode(phone,code);
        return Result.success("验证码验证成功");
    }
}
