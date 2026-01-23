package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Resident;
import com.maple.vms.vo.model.ResidentModel;
import com.maple.vms.vo.query.ResidentPageQuery;

import java.util.List;

/**
 * 农村人口档案服务接口.
 */
public interface IVmsResidentService extends IService<Resident> {

    /**
     * 分页查询居民信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ResidentModel> getPageList(ResidentPageQuery query);

    /**
     * 获取居民列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 居民列表
     */
    List<ResidentModel> getList(ResidentPageQuery query);

    /**
     * 根据ID获取居民信息.
     *
     * @param id 主键
     * @return 居民信息
     */
    ResidentModel getResidentById(Long id);

    /**
     * 新增居民信息.
     *
     * @param model 居民模型
     * @return 主键ID
     */
    Long createResident(ResidentModel model);

    /**
     * 修改居民信息.
     *
     * @param model 居民模型
     */
    void updateResident(ResidentModel model);

    /**
     * 删除居民信息.
     *
     * @param id 主键
     */
    void deleteResident(Long id);

    /**
     * 按户分组查询居民信息（用于户籍分组展示）.
     *
     * @param query 查询条件
     * @return 按户分组的居民列表
     */
    List<ResidentModel> getResidentsByFamily(ResidentPageQuery query);

    /**
     * 迁出操作.
     *
     * @param id 居民ID
     * @param migrateTo 迁出地
     */
    void migrateOut(Long id, String migrateTo);

    /**
     * 迁入操作.
     *
     * @param id 居民ID
     */
    void migrateIn(Long id);
}

