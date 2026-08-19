package com.gongdi.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考勤动作请求对象，经纬度字段预留给后续电子围栏校验。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
public class AttendanceActionDTO {

    /**
     * 项目 ID
     */
    private Long projectId;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;
}
