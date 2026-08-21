package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.Attendance;
import com.gongdi.mapper.AttendanceMapper;
import com.gongdi.service.IAttendanceService;
import org.springframework.stereotype.Service;

/**
 * 考勤记录 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {
}