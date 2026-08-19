package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，对应 sys_user 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    /** 主键 */
    private Long id;

    /** 姓名 */
    private String name;

    /** 微信 openid */
    private String openid;

    /** 头像 */
    private String avatar;

    /** 手机号 */
    private String phone;

    /** 角色（前端展示用） */
    private String role;

    /** 岗位 */
    private String position;

    /** 当前项目名称 */
    private String currentProject;

    /** 状态：1 正常，0 停用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}