package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.ProjectTeam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目班组 Mapper，对应 project_team 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ProjectTeamMapper extends BaseMapper<ProjectTeam> {
}