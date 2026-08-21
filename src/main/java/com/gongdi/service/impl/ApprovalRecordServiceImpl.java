package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.ApprovalRecord;
import com.gongdi.mapper.ApprovalRecordMapper;
import com.gongdi.service.IApprovalRecordService;
import org.springframework.stereotype.Service;

/**
 * 审批记录 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord> implements IApprovalRecordService {
}