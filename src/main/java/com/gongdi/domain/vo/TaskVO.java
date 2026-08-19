package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务响应对象，包含前端展示所需的中文状态和颜色标识。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    /**
     * 任务 ID
     */
    private Long id;

    /**
     * 展示序号
     */
    private int index;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务状态码
     */
    private String status;

    /**
     * 状态中文文案
     */
    private String statusText;

    /**
     * 状态主题色标识
     */
    private String statusColor;
}
