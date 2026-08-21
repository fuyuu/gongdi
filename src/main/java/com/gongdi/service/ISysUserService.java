package com.gongdi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gongdi.domain.dto.UpdateUserDTO;
import com.gongdi.domain.entity.SysUser;
import com.gongdi.domain.vo.UserVO;

/**
 * 用户 Service 接口。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户 ID 获取用户信息（含手机号、岗位、当前项目），返回面向小程序「我的」的视图对象。
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 更新用户资料（姓名、头像），仅更新非空字段。
     *
     * @param dto 用户资料更新入参
     */
    void updateProfile(UpdateUserDTO dto);

    /**
     * 绑定手机号：用微信手机号授权凭证换取纯手机号，并写入 sys_user_phone。
     *
     * @param userId    当前用户 ID
     * @param phoneCode getPhoneNumber 返回的一次性手机号授权凭证
     * @return 绑定成功的手机号
     */
    String bindPhone(Long userId, String phoneCode);
}
