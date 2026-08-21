package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 企业实体，对应 enterprise 表。
 * 企业是系统中的基础组织，后续项目、人员等数据通过 enterprise_id 关联。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("enterprise")
public class Enterprise {

    /** 主键，使用雪花算法生成 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 企业名称 */
    private String enterpriseName;

    /** 企业编号 */
    private String enterpriseCode;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 企业地址 */
    private String address;

    /** 状态：ACTIVE / DISABLED */
    private String status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}
