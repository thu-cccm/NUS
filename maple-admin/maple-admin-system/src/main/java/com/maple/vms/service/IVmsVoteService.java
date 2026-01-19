package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Vote;
import com.maple.vms.vo.model.VoteModel;
import com.maple.vms.vo.query.VotePageQuery;

/**
 * 民主互动服务接口.
 */
public interface IVmsVoteService extends IService<Vote> {

    /**
     * 分页查询投票议题.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<VoteModel> getPageList(VotePageQuery query);

    /**
     * 根据ID获取投票议题.
     *
     * @param id 主键
     * @return 投票议题
     */
    VoteModel getVoteById(Long id);

    /**
     * 新增投票议题.
     *
     * @param model 投票模型
     * @return 主键ID
     */
    Long createVote(VoteModel model);

    /**
     * 修改投票议题.
     *
     * @param model 投票模型
     */
    void updateVote(VoteModel model);

    /**
     * 删除投票议题.
     *
     * @param id 主键
     */
    void deleteVote(Long id);

    /**
     * 进行投票.
     *
     * @param id 投票ID
     * @param agree 是否赞成
     */
    void castVote(Long id, boolean agree);

    /**
     * 结束投票.
     *
     * @param id 投票ID
     */
    void endVote(Long id);
}

