package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.ApprovalRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批记录 Mapper，对应 approval_record 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ApprovalRecordMapper extends BaseMapper<ApprovalRecord> {
}