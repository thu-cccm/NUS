package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Land;
import com.maple.vms.vo.model.LandModel;
import com.maple.vms.vo.query.LandPageQuery;

/**
 * 土地资源服务接口.
 */
public interface IVmsLandService extends IService<Land> {

    /**
     * 分页查询土地信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<LandModel> getPageList(LandPageQuery query);

    /**
     * 根据ID获取土地信息.
     *
     * @param id 主键
     * @return 土地信息
     */
    LandModel getLandById(Long id);

    /**
     * 新增土地信息.
     *
     * @param model 土地模型
     * @return 主键ID
     */
    Long createLand(LandModel model);

    /**
     * 修改土地信息.
     *
     * @param model 土地模型
     */
    void updateLand(LandModel model);

    /**
     * 删除土地信息.
     *
     * @param id 主键
     */
    void deleteLand(Long id);
}

