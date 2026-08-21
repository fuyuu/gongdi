package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.ProjectIncome;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目收入 Mapper，对应 project_income 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ProjectIncomeMapper extends BaseMapper<ProjectIncome> {
}