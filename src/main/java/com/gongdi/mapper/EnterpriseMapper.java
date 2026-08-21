package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业 Mapper，对应 enterprise 表。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {
}
