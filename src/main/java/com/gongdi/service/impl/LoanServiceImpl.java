package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.Loan;
import com.gongdi.mapper.LoanMapper;
import com.gongdi.service.ILoanService;
import org.springframework.stereotype.Service;

/**
 * 借款记录 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan> implements ILoanService {
}