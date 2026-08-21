package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 企业响应对象，面向企业列表和详情。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseVO {

    /**
     * 企业 ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业编号
     */
    private String enterpriseCode;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 状态：ACTIVE / DISABLED
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
