package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 附件实体，对应 attachment 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    /** 主键 */
    private Long id;

    /** 业务类型：ATTENDANCE / DAILY_REPORT / EXPENSE / LOAN */
    private String bizType;

    /** 业务 ID */
    private Long bizId;

    /** 文件类型：PHOTO / INVOICE / RECEIPT / OTHER */
    private String fileType;

    /** 文件名 */
    private String fileName;

    /** 文件 URL */
    private String fileUrl;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}