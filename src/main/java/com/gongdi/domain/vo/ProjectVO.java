package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目响应对象，面向小程序项目卡片和项目切换。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO {

    /**
     * 项目 ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 施工区域
     */
    private String area;

    /**
     * 项目状态
     */
    private String status;
}
