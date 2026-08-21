package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.ProjectMember;
import com.gongdi.mapper.ProjectMemberMapper;
import com.gongdi.service.IProjectMemberService;
import org.springframework.stereotype.Service;

/**
 * 项目成员 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements IProjectMemberService {
}