package com.gongdi.domain.dto;

import lombok.Data;

/**
 * 用户资料更新入参，只允许修改姓名、头像等本人可编辑字段。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
public class UpdateUserDTO {

    /**
     * 用户 ID，由后端从登录态填充，前端无需传
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像地址
     */
    private String avatar;
}
