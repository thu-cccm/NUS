package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.PartyScore;
import com.maple.vms.vo.model.PartyScoreModel;
import com.maple.vms.vo.query.PartyScorePageQuery;

import java.util.List;
import java.util.Map;

/**
 * 党员积分服务接口.
 */
public interface IVmsPartyScoreService extends IService<PartyScore> {

    /**
     * 分页查询党员积分信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<PartyScoreModel> getPageList(PartyScorePageQuery query);

    /**
     * 获取党员积分列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 党员积分列表
     */
    List<PartyScoreModel> getList(PartyScorePageQuery query);

    /**
     * 根据ID获取党员积分信息.
     *
     * @param id 主键
     * @return 党员积分信息
     */
    PartyScoreModel getPartyScoreById(Long id);

    /**
     * 新增党员积分信息.
     *
     * @param model 党员积分模型
     * @return 主键ID
     */
    Long createPartyScore(PartyScoreModel model);

    /**
     * 修改党员积分信息.
     *
     * @param model 党员积分模型
     */
    void updatePartyScore(PartyScoreModel model);

    /**
     * 删除党员积分信息.
     *
     * @param id 主键
     */
    void deletePartyScore(Long id);

    /**
     * 获取党员积分排行榜（总积分）.
     *
     * @param limit 前N名
     * @return 排行榜列表
     */
    List<Map<String, Object>> getScoreRanking(Integer limit);
}

