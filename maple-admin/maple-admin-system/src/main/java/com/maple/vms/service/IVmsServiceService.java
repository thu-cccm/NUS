package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Service;
import com.maple.vms.vo.model.ServiceModel;
import com.maple.vms.vo.query.ServicePageQuery;

import java.util.List;

/**
 * 便民服务服务接口.
 */
public interface IVmsServiceService extends IService<Service> {

    /**
     * 分页查询便民服务信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ServiceModel> getPageList(ServicePageQuery query);

    /**
     * 获取便民服务列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 便民服务列表
     */
    List<ServiceModel> getList(ServicePageQuery query);

    /**
     * 根据ID获取便民服务信息.
     *
     * @param id 主键
     * @return 便民服务信息
     */
    ServiceModel getServiceById(Long id);

    /**
     * 新增便民服务信息.
     *
     * @param model 便民服务模型
     * @return 主键ID
     */
    Long createService(ServiceModel model);

    /**
     * 修改便民服务信息.
     *
     * @param model 便民服务模型
     */
    void updateService(ServiceModel model);

    /**
     * 删除便民服务信息.
     *
     * @param id 主键
     */
    void deleteService(Long id);
}

