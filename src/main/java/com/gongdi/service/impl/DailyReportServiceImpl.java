package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.DailyReport;
import com.gongdi.mapper.DailyReportMapper;
import com.gongdi.service.IDailyReportService;
import org.springframework.stereotype.Service;

/**
 * 施工日报 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class DailyReportServiceImpl extends ServiceImpl<DailyReportMapper, DailyReport> implements IDailyReportService {
}