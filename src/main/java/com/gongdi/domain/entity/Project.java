package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目实体，对应 project 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    /** 主键 */
    private Long id;

    /** 项目名称 */
    private String projectName;

    /** 项目编号 */
    private String projectCode;

    /** 项目地址 */
    private String address;

    /** 项目负责人 */
    private Long managerId;

    /** 开工日期 */
    private LocalDate startDate;

    /** 计划完工日期 */
    private LocalDate endDate;

    /** 项目经度 */
    private BigDecimal longitude;

    /** 项目纬度 */
    private BigDecimal latitude;

    /** 签到范围（米） */
    private Integer signRadius;

    /** 状态：ACTIVE / PAUSED / FINISHED */
    private String status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}