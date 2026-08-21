package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Expense;
import org.apache.ibatis.annotations.Mapper;

/**
 * 费用报销 Mapper，对应 expense 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface ExpenseMapper extends BaseMapper<Expense> {
}