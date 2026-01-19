package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Infrastructure;
import com.maple.vms.vo.model.InfrastructureModel;
import com.maple.vms.vo.query.InfrastructurePageQuery;

/**
 * 基础设施服务接口.
 */
public interface IVmsInfrastructureService extends IService<Infrastructure> {

    /**
     * 分页查询基础设施信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<InfrastructureModel> getPageList(InfrastructurePageQuery query);

    /**
     * 根据ID获取基础设施信息.
     *
     * @param id 主键
     * @return 基础设施信息
     */
    InfrastructureModel getInfrastructureById(Long id);

    /**
     * 新增基础设施信息.
     *
     * @param model 基础设施模型
     * @return 主键ID
     */
    Long createInfrastructure(InfrastructureModel model);

    /**
     * 修改基础设施信息.
     *
     * @param model 基础设施模型
     */
    void updateInfrastructure(InfrastructureModel model);

    /**
     * 删除基础设施信息.
     *
     * @param id 主键
     */
    void deleteInfrastructure(Long id);
}

