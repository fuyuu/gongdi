package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("approval_record")
public class ApprovalRecord {

    /** 主键，使用雪花算法生成 */
    @TableId(type = IdType.ASSIGN_ID)
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