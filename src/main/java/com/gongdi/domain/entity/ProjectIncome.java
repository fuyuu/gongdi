package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目收入实体，对应 project_income 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectIncome {

    /** 主键 */
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 收款日期 */
    private LocalDate incomeDate;

    /** 收入金额 */
    private BigDecimal amount;

    /** 付款方 */
    private String payerName;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}