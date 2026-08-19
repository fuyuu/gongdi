package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 借款记录实体，对应 loan 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    /** 主键 */
    private Long id;

    /** 借款单号 */
    private String loanNo;

    /** 项目 ID */
    private Long projectId;

    /** 借款人 */
    private Long userId;

    /** 借款金额 */
    private BigDecimal amount;

    /** 借款用途 */
    private String purpose;

    /** 预计核销日期 */
    private LocalDate expectedDate;

    /** 已退回金额 */
    private BigDecimal returnedAmount;

    /** 状态：PENDING / APPROVED / PAID / SETTLED / REJECTED */
    private String status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}