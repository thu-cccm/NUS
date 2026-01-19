package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Apply;
import com.maple.vms.vo.model.ApplyModel;
import com.maple.vms.vo.query.ApplyAuditQuery;
import com.maple.vms.vo.query.ApplyPageQuery;

/**
 * 事务申请服务接口.
 */
public interface IVmsApplyService extends IService<Apply> {

    /**
     * 分页查询事务申请.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ApplyModel> getPageList(ApplyPageQuery query);

    /**
     * 根据ID获取事务申请.
     *
     * @param id 主键
     * @return 事务申请
     */
    ApplyModel getApplyById(Long id);

    /**
     * 新增事务申请.
     *
     * @param model 事务申请模型
     * @return 主键ID
     */
    Long createApply(ApplyModel model);

    /**
     * 修改事务申请.
     *
     * @param model 事务申请模型
     */
    void updateApply(ApplyModel model);

    /**
     * 删除事务申请.
     *
     * @param id 主键
     */
    void deleteApply(Long id);

    /**
     * 审核事务申请.
     *
     * @param query 审核参数
     */
    void auditApply(ApplyAuditQuery query);
}

