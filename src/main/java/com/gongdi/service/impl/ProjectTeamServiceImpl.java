package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.ProjectTeam;
import com.gongdi.mapper.ProjectTeamMapper;
import com.gongdi.service.IProjectTeamService;
import org.springframework.stereotype.Service;

/**
 * 项目班组 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ProjectTeamServiceImpl extends ServiceImpl<ProjectTeamMapper, ProjectTeam> implements IProjectTeamService {
}