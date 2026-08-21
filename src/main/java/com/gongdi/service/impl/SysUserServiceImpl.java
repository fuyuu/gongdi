package com.gongdi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.dto.UpdateUserDTO;
import com.gongdi.domain.entity.Project;
import com.gongdi.domain.entity.ProjectMember;
import com.gongdi.domain.entity.SysUser;
import com.gongdi.domain.entity.SysUserPhone;
import com.gongdi.domain.vo.UserVO;
import com.gongdi.exception.BusinessException;
import com.gongdi.mapper.UserMapper;
import com.gongdi.service.IProjectMemberService;
import com.gongdi.service.IProjectService;
import com.gongdi.service.ISysUserPhoneService;
import com.gongdi.service.ISysUserService;
import com.gongdi.service.IWechatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements ISysUserService {

    private final ISysUserPhoneService sysUserPhoneService;
    private final IWechatService wechatService;
    private final IProjectMemberService projectMemberService;
    private final IProjectService projectService;

    @Override
    public UserVO getUserInfo(Long userId) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setAvatar(user.getAvatar());
        vo.setRole(resolveUserRoleLabel(user.getRole()));

        // 手机号单独存于 sys_user_phone，按 user_id 查询，避免暴露 openid 等敏感字段
        SysUserPhone userPhone = sysUserPhoneService.getOne(
                new LambdaQueryWrapper<SysUserPhone>().eq(SysUserPhone::getUserId, userId));
        if (userPhone != null && userPhone.getPhone() != null) {
            vo.setPhone(maskPhone(String.valueOf(userPhone.getPhone())));
        }

        // 岗位与当前项目来自 project_member：取该用户当前有效的第一条成员关系
        ProjectMember member = projectMemberService.getOne(
                new LambdaQueryWrapper<ProjectMember>()
                        .eq(ProjectMember::getUserId, userId)
                        .eq(ProjectMember::getStatus, 1)
                        .last("LIMIT 1"));
        if (member != null) {
            vo.setPosition(resolveRoleLabel(member.getRoleCode()));
            if (member.getProjectId() != null) {
                Project project = projectService.getById(member.getProjectId());
                if (project != null) {
                    vo.setCurrentProject(project.getProjectName());
                }
            }
        }
        return vo;
    }

    @Override
    public void updateProfile(UpdateUserDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (dto.getName() != null && (dto.getName().isBlank() || dto.getName().length() > 50)) {
            throw new BusinessException("姓名不能为空且不超过50字");
        }

        SysUser user = new SysUser();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setAvatar(dto.getAvatar());
        // updateById 默认忽略 null 字段，仅更新非空内容
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindPhone(Long userId, String phoneCode) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        String phone = wechatService.getPhoneNumber(phoneCode);
        if (phone == null || !phone.matches("^1\\d{10}$")) {
            throw new BusinessException("手机号格式不正确");
        }

        Long phoneLong = Long.parseLong(phone);

        // 同一手机号只能绑定一个账号，防止手机号被复用导致越权
        SysUserPhone bound = sysUserPhoneService.getOne(
                new LambdaQueryWrapper<SysUserPhone>().eq(SysUserPhone::getPhone, phoneLong));
        if (bound != null && !bound.getUserId().equals(userId)) {
            throw new BusinessException("该手机号已被其他账号绑定");
        }

        // 注册时已创建空手机号记录，这里按 user_id 更新；不存在则兜底新增
        boolean updated = sysUserPhoneService.update(
                new LambdaUpdateWrapper<SysUserPhone>()
                        .eq(SysUserPhone::getUserId, userId)
                        .set(SysUserPhone::getPhone, phoneLong)
                        .set(SysUserPhone::getStatus, 1));
        if (!updated) {
            SysUserPhone userPhone = new SysUserPhone();
            userPhone.setUserId(userId);
            userPhone.setPhone(phoneLong);
            userPhone.setStatus(1);
            sysUserPhoneService.save(userPhone);
        }
        return phone;
    }

    /**
     * 将项目成员角色编码转换为中文岗位名，未识别的编码原样返回。
     */
    private String resolveRoleLabel(String roleCode) {
        if (roleCode == null) {
            return null;
        }
        return switch (roleCode) {
            case "BOSS" -> "老板/负责人";
            case "MANAGER" -> "项目经理";
            case "TEAM_LEADER" -> "班组长";
            case "WORKER" -> "工人";
            case "FINANCE" -> "财务";
            default -> roleCode;
        };
    }

    /**
     * 将 sys_user.role 角色编码转换为中文名，未识别的编码返回空。
     */
    private String resolveUserRoleLabel(Integer role) {
        if (role == null) {
            return null;
        }
        return switch (role) {
            case 1 -> "用户";
            case 2 -> "项目管理员";
            case 3 -> "系统管理员";
            case 4 -> "禁用用户";
            default -> null;
        };
    }

    /**
     * 手机号脱敏：保留前 3 位与后 4 位，中间用 * 遮蔽，如 138****1234。
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
}
