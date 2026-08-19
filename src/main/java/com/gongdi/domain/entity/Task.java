package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务实体，保存任务归属项目、负责人和当前状态。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    /**
     * 任务 ID
     */
    private Long id;

    /**
     * 归属项目 ID
     */
    private Long projectId;

    /**
     * 负责人用户 ID
     */
    private Long ownerUserId;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务状态
     */
    private String status;
}
