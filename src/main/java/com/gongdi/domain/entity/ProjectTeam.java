package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("project_team")
public class ProjectTeam {

    /** 主键，数据库自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 班组名称 */
    private String teamName;

    /** 工种 */
    private String workType;

    /** 班组负责人 */
    private Long leaderId;

    /** 状态：1 正常，0 停用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}
