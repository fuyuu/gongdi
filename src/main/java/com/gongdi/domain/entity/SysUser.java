package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_user")
public class SysUser {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 姓名 */
    private String name;

    /** 微信 openid */
    private String openid;

    private String unionid;

    /** 头像 */
    private String avatar;

    /** 角色：1-用户，2-系统管理员，4-该用户禁止使用 */
    private Integer role;

    /** 状态：1 正常，0 停用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}