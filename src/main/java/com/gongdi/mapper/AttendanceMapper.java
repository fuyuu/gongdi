package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤记录 Mapper，对应 attendance 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
}