package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目成员实体，对应 project_member 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    /** 主键 */
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 用户 ID */
    private Long userId;

    /** 班组 ID */
    private Long teamId;

    /** 角色编码：BOSS / MANAGER / TEAM_LEADER / WORKER / FINANCE */
    private String roleCode;

    /** 工种 */
    private String workType;

    /** 进场日期 */
    private LocalDate joinDate;

    /** 离场日期 */
    private LocalDate leaveDate;

    /** 状态：1 正常 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}