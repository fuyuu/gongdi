package com.gongdi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gongdi.domain.dto.EnterpriseCreateDTO;
import com.gongdi.domain.dto.EnterpriseQueryDTO;
import com.gongdi.domain.dto.EnterpriseUpdateDTO;
import com.gongdi.domain.entity.Enterprise;
import com.gongdi.domain.vo.EnterpriseVO;

/**
 * 企业 Service 接口。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
public interface IEnterpriseService extends IService<Enterprise> {

    /**
     * 创建企业：校验企业编号唯一后保存。
     *
     * @param dto 企业创建入参
     * @return 创建后的企业信息
     */
    EnterpriseVO createEnterprise(EnterpriseCreateDTO dto);

    /**
     * 分页查询企业列表，支持按名称模糊、编号精确、状态筛选。
     *
     * @param dto 查询条件
     * @return 企业分页数据
     */
    Page<EnterpriseVO> pageEnterprise(EnterpriseQueryDTO dto);

    /**
     * 查询企业详情。
     *
     * @param id 企业 ID
     * @return 企业详细信息
     */
    EnterpriseVO getEnterprise(Long id);

    /**
     * 修改企业：企业编号变更时重新校验编号唯一。
     *
     * @param id  企业 ID
     * @param dto 修改入参
     */
    void updateEnterprise(Long id, EnterpriseUpdateDTO dto);

    /**
     * 删除企业：逻辑删除；存在关联项目时禁止删除。
     *
     * @param id 企业 ID
     */
    void deleteEnterprise(Long id);
}
