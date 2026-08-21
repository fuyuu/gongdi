package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.DailyReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 施工日报 Mapper，对应 daily_report 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface DailyReportMapper extends BaseMapper<DailyReport> {
}