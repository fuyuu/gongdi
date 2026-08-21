package com.gongdi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.dto.EnterpriseCreateDTO;
import com.gongdi.domain.dto.EnterpriseQueryDTO;
import com.gongdi.domain.dto.EnterpriseUpdateDTO;
import com.gongdi.domain.entity.Enterprise;
import com.gongdi.domain.entity.Project;
import com.gongdi.domain.vo.EnterpriseVO;
import com.gongdi.exception.BusinessException;
import com.gongdi.mapper.EnterpriseMapper;
import com.gongdi.service.IEnterpriseService;
import com.gongdi.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 企业 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

    private final IProjectService projectService;

    @Override
    public EnterpriseVO createEnterprise(EnterpriseCreateDTO dto) {
        // 企业编号全局唯一，保存前先校验，避免依赖唯一索引报错
        checkCodeDuplicate(dto.getEnterpriseCode(), null);

        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseName(dto.getEnterpriseName());
        enterprise.setEnterpriseCode(dto.getEnterpriseCode());
        enterprise.setContactPerson(dto.getContactPerson());
        enterprise.setContactPhone(dto.getContactPhone());
        enterprise.setAddress(dto.getAddress());
        // 新企业默认正常状态
        enterprise.setStatus("ACTIVE");
        save(enterprise);
        return toVO(enterprise);
    }

    @Override
    public Page<EnterpriseVO> pageEnterprise(EnterpriseQueryDTO dto) {
        Page<Enterprise> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Enterprise> wrapper = new LambdaQueryWrapper<Enterprise>()
                .like(StrUtil.isNotBlank(dto.getEnterpriseName()), Enterprise::getEnterpriseName, dto.getEnterpriseName())
                .eq(StrUtil.isNotBlank(dto.getEnterpriseCode()), Enterprise::getEnterpriseCode, dto.getEnterpriseCode())
                .eq(StrUtil.isNotBlank(dto.getStatus()), Enterprise::getStatus, dto.getStatus())
                .orderByDesc(Enterprise::getCreateTime);
        Page<Enterprise> enterprisePage = page(page, wrapper);
        Page<EnterpriseVO> voPage = new Page<>(enterprisePage.getCurrent(), enterprisePage.getSize(), enterprisePage.getTotal());
        voPage.setRecords(enterprisePage.getRecords().stream().map(this::toVO).toList());
        return voPage;
    }

    @Override
    public EnterpriseVO getEnterprise(Long id) {
        Enterprise enterprise = getById(id);
        if (enterprise == null) {
            throw new BusinessException("企业不存在");
        }
        return toVO(enterprise);
    }

    @Override
    public void updateEnterprise(Long id, EnterpriseUpdateDTO dto) {
        Enterprise exist = getById(id);
        if (exist == null) {
            throw new BusinessException("企业不存在");
        }

        // 企业编号变更时，需要排除自身后重新校验编号唯一
        if (StrUtil.isNotBlank(dto.getEnterpriseCode())) {
            checkCodeDuplicate(dto.getEnterpriseCode(), id);
        }

        Enterprise enterprise = new Enterprise();
        enterprise.setId(id);
        enterprise.setEnterpriseName(dto.getEnterpriseName());
        enterprise.setEnterpriseCode(dto.getEnterpriseCode());
        enterprise.setContactPerson(dto.getContactPerson());
        enterprise.setContactPhone(dto.getContactPhone());
        enterprise.setAddress(dto.getAddress());
        enterprise.setStatus(dto.getStatus());
        // updateById 默认忽略 null 字段，仅更新非空内容
        updateById(enterprise);
    }

    @Override
    public void deleteEnterprise(Long id) {
        Enterprise exist = getById(id);
        if (exist == null) {
            throw new BusinessException("企业不存在");
        }

        // 企业下已存在项目等业务数据时不允许删除，避免项目数据失去归属，可改为停用
        long projectCount = projectService.count(new LambdaQueryWrapper<Project>()
                .eq(Project::getEnterpriseId, id)
                .eq(Project::getDeleted, 0));
        if (projectCount > 0) {
            throw new BusinessException("该企业下已存在项目，无法删除，请改为停用");
        }

        // 逻辑删除，企业不再出现在正常列表中
        update(new LambdaUpdateWrapper<Enterprise>()
                .eq(Enterprise::getId, id)
                .set(Enterprise::getDeleted, 1));
    }

    /**
     * 校验企业编号是否已被其他企业占用。
     *
     * @param code        企业编号
     * @param excludeId 修改场景下排除的企业 ID，创建时传 null
     */
    private void checkCodeDuplicate(String code, Long excludeId) {
        long count = count(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseCode, code)
                .ne(excludeId != null, Enterprise::getId, excludeId));
        if (count > 0) {
            throw new BusinessException("企业编号已存在");
        }
    }

    /**
     * 实体转响应对象。
     */
    private EnterpriseVO toVO(Enterprise enterprise) {
        EnterpriseVO vo = new EnterpriseVO();
        vo.setId(enterprise.getId());
        vo.setEnterpriseName(enterprise.getEnterpriseName());
        vo.setEnterpriseCode(enterprise.getEnterpriseCode());
        vo.setContactPerson(enterprise.getContactPerson());
        vo.setContactPhone(enterprise.getContactPhone());
        vo.setAddress(enterprise.getAddress());
        vo.setStatus(enterprise.getStatus());
        vo.setCreateTime(enterprise.getCreateTime());
        vo.setUpdateTime(enterprise.getUpdateTime());
        return vo;
    }
}
