package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Agriculture;
import com.maple.vms.vo.model.AgricultureModel;
import com.maple.vms.vo.query.AgriculturePageQuery;

import java.util.List;

/**
 * 农业生产服务接口.
 */
public interface IVmsAgricultureService extends IService<Agriculture> {

    /**
     * 分页查询农业生产信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AgricultureModel> getPageList(AgriculturePageQuery query);

    /**
     * 获取农业生产列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 农业生产列表
     */
    List<AgricultureModel> getList(AgriculturePageQuery query);

    /**
     * 根据ID获取农业生产信息.
     *
     * @param id 主键
     * @return 农业生产信息
     */
    AgricultureModel getAgricultureById(Long id);

    /**
     * 新增农业生产信息.
     *
     * @param model 农业生产模型
     * @return 主键ID
     */
    Long createAgriculture(AgricultureModel model);

    /**
     * 修改农业生产信息.
     *
     * @param model 农业生产模型
     */
    void updateAgriculture(AgricultureModel model);

    /**
     * 删除农业生产信息.
     *
     * @param id 主键
     */
    void deleteAgriculture(Long id);
}

