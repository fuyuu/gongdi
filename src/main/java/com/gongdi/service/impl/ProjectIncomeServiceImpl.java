package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.ProjectIncome;
import com.gongdi.mapper.ProjectIncomeMapper;
import com.gongdi.service.IProjectIncomeService;
import org.springframework.stereotype.Service;

/**
 * 项目收入 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ProjectIncomeServiceImpl extends ServiceImpl<ProjectIncomeMapper, ProjectIncome> implements IProjectIncomeService {
}