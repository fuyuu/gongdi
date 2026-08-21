package com.gongdi.controller;

import com.gongdi.domain.dto.UpdateUserDTO;
import com.gongdi.domain.vo.Result;
import com.gongdi.domain.vo.UserVO;
import com.gongdi.service.ISysUserService;
import com.gongdi.util.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器：查询当前用户信息、修改本人资料。
 * 当前用户 ID 一律从登录态（JWT claims）获取，不信任前端传入的 userId。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final ISysUserService sysUserService;

    /**
     * 获取当前登录用户信息。
     */
    @GetMapping("/info")
    public Result<UserVO> info() {
        Long userId = ThreadLocalUtils.getCurrentUserId();
        UserVO userInfo = sysUserService.getUserInfo(userId);
        log.info("用户信息获取成功, userId: {}", userId);
        return Result.success(userInfo);
    }

    /**
     * 修改当前登录用户资料（姓名、头像）。
     */
    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody UpdateUserDTO dto) {
        Long userId = ThreadLocalUtils.getCurrentUserId();
        dto.setId(userId);
        sysUserService.updateProfile(dto);
        log.info("用户资料更新成功, userId: {}", userId);
        return Result.success("用户资料更新成功");
    }

    /**
     * 绑定手机号：用 getPhoneNumber 返回的手机号授权凭证换取并绑定手机号。
     */
    @PostMapping("/phone")
    public Result<String> bindPhone(@RequestParam String phoneCode) {
        Long userId = ThreadLocalUtils.getCurrentUserId();
        String phone = sysUserService.bindPhone(userId, phoneCode);
        log.info("手机号绑定成功, userId: {}", userId);
        return Result.success(phone, "手机号绑定成功");
    }
}
