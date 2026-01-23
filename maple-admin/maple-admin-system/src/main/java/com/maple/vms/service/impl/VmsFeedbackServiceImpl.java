package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Feedback;
import com.maple.vms.mapper.FeedbackMapper;
import com.maple.vms.service.IVmsFeedbackService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.FeedbackModel;
import com.maple.vms.vo.query.FeedbackPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 村民互动服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsFeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IVmsFeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Override
    public IPage<FeedbackModel> getPageList(FeedbackPageQuery query) {
        Page<Feedback> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        IPage<Feedback> result = feedbackMapper.selectPage(page, buildWrapper(query));
        return toModelPage(result, FeedbackModel.class);
    }

    @Override
    public List<FeedbackModel> getList(FeedbackPageQuery query) {
        List<Feedback> list = feedbackMapper.selectList(buildWrapper(query));
        return TransformUtils.mapList(list, FeedbackModel.class);
    }

    @Override
    public Long createFeedback(FeedbackModel model) {
        Feedback feedback = TransformUtils.map(model, Feedback.class);
        if (VmsAuthUtils.isVillager()) {
            feedback.setResidentId(JwtUtil.getUserId());
        }
        if (feedback.getIsPublic() == null) {
            feedback.setIsPublic(1);
        }
        if (feedback.getReportCount() == null) {
            feedback.setReportCount(0);
        }
        if (feedback.getIsHidden() == null) {
            feedback.setIsHidden(0);
        }
        feedbackMapper.insert(feedback);
        return feedback.getId();
    }

    @Override
    public void replyFeedback(Long id, String replyContent) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可回复留言");
        }
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        feedback.setReplyContent(replyContent);
        feedback.setReplyTime(new Date());
        feedbackMapper.updateById(feedback);
    }

    @Override
    public void reportFeedback(Long id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        feedback.setReportCount(feedback.getReportCount() == null ? 1 : feedback.getReportCount() + 1);
        feedbackMapper.updateById(feedback);
    }

    @Override
    public void hideFeedback(Long id, boolean hidden) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可隐藏留言");
        }
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        feedback.setIsHidden(hidden ? 1 : 0);
        feedbackMapper.updateById(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可删除留言");
        }
        feedbackMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Feedback> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }

    private com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Feedback> buildWrapper(FeedbackPageQuery query) {
        FeedbackModel model = Objects.isNull(query) ? null : query.getQuery();
        String content = model == null ? null : model.getContent();
        Integer isPublic = model == null ? null : model.getIsPublic();
        Integer isHidden = model == null ? null : model.getIsHidden();
        Boolean hasReply = model == null ? null : model.getHasReply();

        return Wrappers.lambdaQuery(Feedback.class)
                .like(StringUtils.isNotBlank(content), Feedback::getContent, content)
                .eq(Objects.nonNull(isPublic), Feedback::getIsPublic, isPublic)
                .eq(Boolean.TRUE.equals(JwtUtil.isAdmin()) && Objects.nonNull(isHidden), Feedback::getIsHidden, isHidden)
                .isNotNull(Boolean.TRUE.equals(hasReply), Feedback::getReplyContent)
                .isNull(Boolean.FALSE.equals(hasReply), Feedback::getReplyContent)
                .and(VmsAuthUtils.isVillager(), wrapper -> wrapper
                        .eq(Feedback::getIsPublic, 1)
                        .or()
                        .eq(Feedback::getResidentId, JwtUtil.getUserId()))
                .eq(!Boolean.TRUE.equals(JwtUtil.isAdmin()), Feedback::getIsHidden, 0)
                .orderByDesc(Feedback::getCreateTime);
    }
}

