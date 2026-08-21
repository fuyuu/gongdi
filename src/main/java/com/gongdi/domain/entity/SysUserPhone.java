package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户手机号实体，对应 sys_user_phone 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_phone")
public class SysUserPhone {

    /** 主键，使用雪花算法生成 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 手机号 */
    private Long phone;

    /** 用户 ID */
    private Long userId;

    /** 状态 */
    private Integer status;
}