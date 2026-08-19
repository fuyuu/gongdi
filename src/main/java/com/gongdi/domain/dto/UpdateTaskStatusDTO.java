package com.gongdi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务状态更新请求对象，status 使用后端任务状态码。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
public class UpdateTaskStatusDTO {

    /**
     * 目标状态码
     */
    private String status;
}
