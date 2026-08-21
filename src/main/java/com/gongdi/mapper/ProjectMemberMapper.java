package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.ProjectMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目成员 Mapper，对应 project_member 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {
}