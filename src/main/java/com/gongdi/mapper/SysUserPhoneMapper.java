package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.SysUserPhone;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户手机号 Mapper，对应 sys_user_phone 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface SysUserPhoneMapper extends BaseMapper<SysUserPhone> {
}