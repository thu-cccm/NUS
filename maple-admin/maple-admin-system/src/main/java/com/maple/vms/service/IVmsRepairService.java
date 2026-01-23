package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Repair;
import com.maple.vms.vo.model.RepairModel;
import com.maple.vms.vo.query.RepairPageQuery;

import java.util.List;

/**
 * 设施报修服务接口.
 */
public interface IVmsRepairService extends IService<Repair> {

    /**
     * 分页查询设施报修信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<RepairModel> getPageList(RepairPageQuery query);

    /**
     * 获取设施报修列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 设施报修列表
     */
    List<RepairModel> getList(RepairPageQuery query);

    /**
     * 根据ID获取设施报修信息.
     *
     * @param id 主键
     * @return 设施报修信息
     */
    RepairModel getRepairById(Long id);

    /**
     * 新增设施报修信息.
     *
     * @param model 设施报修模型
     * @return 主键ID
     */
    Long createRepair(RepairModel model);

    /**
     * 修改设施报修信息.
     *
     * @param model 设施报修模型
     */
    void updateRepair(RepairModel model);

    /**
     * 删除设施报修信息.
     *
     * @param id 主键
     */
    void deleteRepair(Long id);

    /**
     * 派单（管理员操作）.
     *
     * @param id 报修ID
     * @param assignee 派单人员
     */
    void assignRepair(Long id, String assignee);

    /**
     * 完成维修（管理员操作）.
     *
     * @param id 报修ID
     * @param repairResult 维修结果
     */
    void completeRepair(Long id, String repairResult);
}

