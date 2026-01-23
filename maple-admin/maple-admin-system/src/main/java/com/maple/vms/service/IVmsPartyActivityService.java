package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.PartyActivity;
import com.maple.vms.vo.model.PartyActivityModel;
import com.maple.vms.vo.query.PartyActivityPageQuery;

import java.util.List;

/**
 * 党建活动服务接口.
 */
public interface IVmsPartyActivityService extends IService<PartyActivity> {

    /**
     * 分页查询党建活动信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PartyActivityModel> getPageList(PartyActivityPageQuery query);

    /**
     * 获取党建活动列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 党建活动列表
     */
    List<PartyActivityModel> getList(PartyActivityPageQuery query);

    /**
     * 根据ID获取党建活动信息.
     *
     * @param id 主键
     * @return 党建活动信息
     */
    PartyActivityModel getPartyActivityById(Long id);

    /**
     * 新增党建活动信息.
     *
     * @param model 党建活动模型
     * @return 主键ID
     */
    Long createPartyActivity(PartyActivityModel model);

    /**
     * 修改党建活动信息.
     *
     * @param model 党建活动模型
     */
    void updatePartyActivity(PartyActivityModel model);

    /**
     * 删除党建活动信息.
     *
     * @param id 主键
     */
    void deletePartyActivity(Long id);
}

