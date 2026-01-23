package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Hazard;
import com.maple.vms.vo.model.HazardModel;
import com.maple.vms.vo.query.HazardPageQuery;

import java.util.List;

/**
 * 安全隐患排查服务接口.
 */
public interface IVmsHazardService extends IService<Hazard> {

    /**
     * 分页查询安全隐患信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<HazardModel> getPageList(HazardPageQuery query);

    /**
     * 获取安全隐患列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 安全隐患列表
     */
    List<HazardModel> getList(HazardPageQuery query);

    /**
     * 根据ID获取安全隐患信息.
     *
     * @param id 主键
     * @return 安全隐患信息
     */
    HazardModel getHazardById(Long id);

    /**
     * 新增安全隐患信息.
     *
     * @param model 安全隐患模型
     * @return 主键ID
     */
    Long createHazard(HazardModel model);

    /**
     * 修改安全隐患信息.
     *
     * @param model 安全隐患模型
     */
    void updateHazard(HazardModel model);

    /**
     * 删除安全隐患信息.
     *
     * @param id 主键
     */
    void deleteHazard(Long id);
}

