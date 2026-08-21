package com.gongdi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序快捷登录入参，仅需 wx.login 返回的临时登录凭证。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Data
public class WxLoginDTO {

    /**
     * wx.login 返回的临时登录 code
     */
    @NotBlank(message = "登录凭证不能为空")
    private String loginCode;
}
