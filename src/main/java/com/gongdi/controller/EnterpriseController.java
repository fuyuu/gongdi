package com.gongdi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongdi.domain.dto.EnterpriseCreateDTO;
import com.gongdi.domain.dto.EnterpriseQueryDTO;
import com.gongdi.domain.dto.EnterpriseUpdateDTO;
import com.gongdi.domain.vo.EnterpriseVO;
import com.gongdi.domain.vo.Result;
import com.gongdi.service.IEnterpriseService;
import com.gongdi.util.ThreadLocalUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 企业控制器：企业的基础增删改查。
 * 企业是系统的基础组织，后续项目、人员等数据通过 enterprise_id 关联。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private final IEnterpriseService enterpriseService;

    /**
     * 创建企业：校验企业名称、编号必填，编号唯一后保存。
     */
    @PostMapping
    public Result<EnterpriseVO> create(@Valid @RequestBody EnterpriseCreateDTO dto) {
        EnterpriseVO vo = enterpriseService.createEnterprise(dto);
        log.info("企业创建成功, enterpriseId: {}, operatorId: {}", vo.getId(), ThreadLocalUtils.getCurrentUserId());
        return Result.success(vo);
    }

    /**
     * 分页查询企业列表，支持按企业名称、编号、状态筛选。
     */
    @GetMapping("/page")
    public Result<Page<EnterpriseVO>> page(@Valid EnterpriseQueryDTO dto) {
        Page<EnterpriseVO> page = enterpriseService.pageEnterprise(dto);
        return Result.success(page);
    }

    /**
     * 查询企业详情。
     */
    @GetMapping("/{id}")
    public Result<EnterpriseVO> detail(@PathVariable Long id) {
        EnterpriseVO vo = enterpriseService.getEnterprise(id);
        return Result.success(vo);
    }

    /**
     * 修改企业：编号变更时后端重新校验编号唯一。
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @Valid @RequestBody EnterpriseUpdateDTO dto) {
        enterpriseService.updateEnterprise(id, dto);
        log.info("企业修改成功, enterpriseId: {}, operatorId: {}", id, ThreadLocalUtils.getCurrentUserId());
        return Result.success("企业修改成功");
    }

    /**
     * 删除企业：逻辑删除，存在关联项目时提示改为停用。
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        enterpriseService.deleteEnterprise(id);
        log.info("企业删除成功, enterpriseId: {}, operatorId: {}", id, ThreadLocalUtils.getCurrentUserId());
        return Result.success("企业删除成功");
    }
}
