package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Project;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目 Mapper，对应 project 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}