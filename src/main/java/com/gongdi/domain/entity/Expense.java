package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用报销实体，对应 expense 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("expense")
public class Expense {

    /** 主键，使用雪花算法生成 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 报销单号 */
    private String expenseNo;

    /** 项目 ID */
    private Long projectId;

    /** 报销人 */
    private Long userId;

    /** 费用类别：LABOR / MATERIAL / TRANSPORT / MEAL / HOTEL / OTHER */
    private String category;

    /** 发生日期 */
    private LocalDate expenseDate;

    /** 金额 */
    private BigDecimal amount;

    /** 收款方 */
    private String payeeName;

    /** 报销说明 */
    private String reason;

    /** 关联借款 ID */
    private Long loanId;

    /** 状态：DRAFT / PENDING / APPROVED / REJECTED / PAID / VOID */
    private String status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}