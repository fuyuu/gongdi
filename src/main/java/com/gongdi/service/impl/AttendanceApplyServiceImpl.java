package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.AttendanceApply;
import com.gongdi.mapper.AttendanceApplyMapper;
import com.gongdi.service.IAttendanceApplyService;
import org.springframework.stereotype.Service;

/**
 * 考勤申请 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class AttendanceApplyServiceImpl extends ServiceImpl<AttendanceApplyMapper, AttendanceApply> implements IAttendanceApplyService {
}