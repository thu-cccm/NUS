package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Feedback;
import com.maple.vms.vo.model.FeedbackModel;
import com.maple.vms.vo.query.FeedbackPageQuery;

import java.util.List;

/**
 * 村民互动服务接口.
 */
public interface IVmsFeedbackService extends IService<Feedback> {

    IPage<FeedbackModel> getPageList(FeedbackPageQuery query);

    List<FeedbackModel> getList(FeedbackPageQuery query);

    Long createFeedback(FeedbackModel model);

    void replyFeedback(Long id, String replyContent);

    void reportFeedback(Long id);

    void hideFeedback(Long id, boolean hidden);

    void deleteFeedback(Long id);
}

