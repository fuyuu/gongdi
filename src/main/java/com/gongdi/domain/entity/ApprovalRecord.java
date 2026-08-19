package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审批记录实体，对应 approval_record 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRecord {

    /** 主键 */
    private Long id;

    /** 业务类型：ATTENDANCE / EXPENSE / LOAN */
    private String bizType;

    /** 业务 ID */
    private Long bizId;

    /** 审批人 */
    private Long approverId;

    /** 动作：APPROVED / REJECTED */
    private String action;

    /** 审批意见 */
    private String opinion;

    /** 审批时间 */
    private LocalDateTime approvalTime;
}