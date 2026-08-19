package com.gongdi.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考勤响应对象，面向今日考勤、签到和签退接口。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceVO {

    /**
     * 是否已签到
     */
    private boolean checkedIn;

    /**
     * 是否已签退
     */
    private boolean checkedOut;

    /**
     * 签到时间文本
     */
    private String checkInTime;

    /**
     * 签退时间文本
     */
    private String checkOutTime;

    /**
     * 本月应出勤天数
     */
    private int monthDays;
}
