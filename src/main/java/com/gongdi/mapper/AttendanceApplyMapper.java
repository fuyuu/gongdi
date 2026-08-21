package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.AttendanceApply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤申请 Mapper，对应 attendance_apply 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface AttendanceApplyMapper extends BaseMapper<AttendanceApply> {
}