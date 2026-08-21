package com.gongdi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 企业创建入参，企业名称和企业编号必填。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
public class EnterpriseCreateDTO {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    @Size(max = 100, message = "企业名称不能超过100字")
    private String enterpriseName;

    /**
     * 企业编号
     */
    @NotBlank(message = "企业编号不能为空")
    @Size(max = 50, message = "企业编号不能超过50字")
    private String enterpriseCode;

    /**
     * 联系人
     */
    @Size(max = 50, message = "联系人不能超过50字")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^1\\d{10}$", message = "联系电话格式不正确")
    private String contactPhone;

    /**
     * 企业地址
     */
    @Size(max = 255, message = "企业地址不能超过255字")
    private String address;
}
