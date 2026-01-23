package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Dispute;
import com.maple.vms.vo.model.DisputeModel;
import com.maple.vms.vo.query.DisputePageQuery;

import java.util.List;

/**
 * 矛盾纠纷调解服务接口.
 */
public interface IVmsDisputeService extends IService<Dispute> {

    /**
     * 分页查询矛盾纠纷信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<DisputeModel> getPageList(DisputePageQuery query);

    /**
     * 获取矛盾纠纷列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 矛盾纠纷列表
     */
    List<DisputeModel> getList(DisputePageQuery query);

    /**
     * 根据ID获取矛盾纠纷信息.
     *
     * @param id 主键
     * @return 矛盾纠纷信息
     */
    DisputeModel getDisputeById(Long id);

    /**
     * 新增矛盾纠纷信息.
     *
     * @param model 矛盾纠纷模型
     * @return 主键ID
     */
    Long createDispute(DisputeModel model);

    /**
     * 修改矛盾纠纷信息.
     *
     * @param model 矛盾纠纷模型
     */
    void updateDispute(DisputeModel model);

    /**
     * 删除矛盾纠纷信息.
     *
     * @param id 主键
     */
    void deleteDispute(Long id);
}

