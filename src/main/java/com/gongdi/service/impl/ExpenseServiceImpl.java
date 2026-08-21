package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.Expense;
import com.gongdi.mapper.ExpenseMapper;
import com.gongdi.service.IExpenseService;
import org.springframework.stereotype.Service;

/**
 * 费用报销 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense> implements IExpenseService {
}