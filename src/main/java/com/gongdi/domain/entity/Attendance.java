package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考勤记录实体，对应 attendance 表。
 *
 * @author Ma Qiang
 * @since 2026/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("attendance")
public class Attendance {

    /** 主键，使用雪花算法生成 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 用户 ID */
    private Long userId;

    /** 考勤日期 */
    private LocalDate attendanceDate;

    /** 签到时间 */
    private LocalDateTime checkInTime;

    /** 签到经度 */
    private BigDecimal checkInLongitude;

    /** 签到纬度 */
    private BigDecimal checkInLatitude;

    /** 签到地址 */
    private String checkInAddress;

    /** 签退时间 */
    private LocalDateTime checkOutTime;

    /** 签退经度 */
    private BigDecimal checkOutLongitude;

    /** 签退纬度 */
    private BigDecimal checkOutLatitude;

    /** 签退地址 */
    private String checkOutAddress;

    /** 状态：NORMAL / LATE / EARLY / LEAVE / ABSENT / ABNORMAL */
    private String status;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    private Integer deleted;
}