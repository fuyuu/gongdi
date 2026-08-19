package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 施工日报实体，对应 daily_report 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyReport {

    /** 主键 */
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 填报人 */
    private Long userId;

    /** 日报日期 */
    private LocalDate reportDate;

    /** 天气 */
    private String weather;

    /** 施工区域 */
    private String constructionArea;

    /** 今日施工内容 */
    private String workContent;

    /** 主要工程量 */
    private BigDecimal workQuantity;

    /** 单位 */
    private String workUnit;

    /** 现场人数 */
    private Integer workerCount;

    /** 现场问题 */
    private String problem;

    /** 明日计划 */
    private String tomorrowPlan;

    /** 状态：DRAFT / SUBMITTED / APPROVED / RETURNED */
    private String status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}