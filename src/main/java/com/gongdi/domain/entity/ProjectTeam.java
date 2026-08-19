package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 项目班组实体，对应 project_team 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeam {

    /** 主键 */
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 班组名称 */
    private String teamName;

    /** 班组长 */
    private Long leaderId;

    /** 工种 */
    private String workType;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}