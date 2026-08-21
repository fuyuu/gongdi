package com.gongdi.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 企业列表查询入参，支持按企业名称、编号、状态筛选，GET 参数绑定。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
public class EnterpriseQueryDTO {

    /**
     * 企业名称，模糊匹配
     */
    private String enterpriseName;

    /**
     * 企业编号，精确匹配
     */
    private String enterpriseCode;

    /**
     * 状态：ACTIVE / DISABLED
     */
    private String status;

    /**
     * 页码，从 1 开始
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;
}
