package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Asset;
import com.maple.vms.vo.model.AssetModel;
import com.maple.vms.vo.query.AssetPageQuery;

import java.util.List;

/**
 * 集体资产服务接口.
 */
public interface IVmsAssetService extends IService<Asset> {

    /**
     * 分页查询资产信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AssetModel> getPageList(AssetPageQuery query);

    /**
     * 获取资产列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 资产列表
     */
    List<AssetModel> getList(AssetPageQuery query);

    /**
     * 根据ID获取资产信息.
     *
     * @param id 主键
     * @return 资产信息
     */
    AssetModel getAssetById(Long id);

    /**
     * 新增资产信息.
     *
     * @param model 资产模型
     * @return 主键ID
     */
    Long createAsset(AssetModel model);

    /**
     * 修改资产信息.
     *
     * @param model 资产模型
     */
    void updateAsset(AssetModel model);

    /**
     * 删除资产信息.
     *
     * @param id 主键
     */
    void deleteAsset(Long id);
}

