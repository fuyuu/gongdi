package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.Project;
import com.gongdi.mapper.ProjectMapper;
import com.gongdi.service.IProjectService;
import org.springframework.stereotype.Service;

/**
 * 项目 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
}