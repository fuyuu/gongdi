package com.gongdi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新访问令牌入参。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Data
public class RefreshTokenDTO {

    /**
     * 登录时下发的刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
