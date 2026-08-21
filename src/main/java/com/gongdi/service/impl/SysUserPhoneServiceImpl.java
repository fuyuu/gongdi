package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.SysUserPhone;
import com.gongdi.mapper.SysUserPhoneMapper;
import com.gongdi.service.ISysUserPhoneService;
import org.springframework.stereotype.Service;

/**
 * 用户手机号 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class SysUserPhoneServiceImpl extends ServiceImpl<SysUserPhoneMapper, SysUserPhone> implements ISysUserPhoneService {
}