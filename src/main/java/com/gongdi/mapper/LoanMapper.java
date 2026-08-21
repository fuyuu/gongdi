package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Loan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 借款记录 Mapper，对应 loan 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface LoanMapper extends BaseMapper<Loan> {
}