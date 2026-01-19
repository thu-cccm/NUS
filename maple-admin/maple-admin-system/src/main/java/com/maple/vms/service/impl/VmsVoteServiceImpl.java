package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Vote;
import com.maple.vms.mapper.VoteMapper;
import com.maple.vms.service.IVmsVoteService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.VoteModel;
import com.maple.vms.vo.query.VotePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 民主互动服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsVoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements IVmsVoteService {

    private final VoteMapper voteMapper;

    @Override
    public IPage<VoteModel> getPageList(VotePageQuery query) {
        VoteModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Vote> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String title = model == null ? null : model.getTitle();
        Integer status = model == null ? null : model.getStatus();
        IPage<Vote> result = voteMapper.selectPage(page, Wrappers.lambdaQuery(Vote.class)
                .like(StringUtils.isNotBlank(title), Vote::getTitle, title)
                .eq(Objects.nonNull(status), Vote::getStatus, status)
                .orderByDesc(Vote::getId));
        return toModelPage(result, VoteModel.class);
    }

    @Override
    public VoteModel getVoteById(Long id) {
        return TransformUtils.map(voteMapper.selectById(id), VoteModel.class);
    }

    @Override
    public Long createVote(VoteModel model) {
        Vote vote = TransformUtils.map(model, Vote.class);
        if (vote.getAgreeCount() == null) {
            vote.setAgreeCount(0);
        }
        if (vote.getDisagreeCount() == null) {
            vote.setDisagreeCount(0);
        }
        if (vote.getStatus() == null) {
            vote.setStatus(0);
        }
        voteMapper.insert(vote);
        return vote.getId();
    }

    @Override
    public void updateVote(VoteModel model) {
        voteMapper.updateById(TransformUtils.map(model, Vote.class));
    }

    @Override
    public void deleteVote(Long id) {
        voteMapper.deleteById(id);
    }

    @Override
    public void castVote(Long id, boolean agree) {
        if (Boolean.TRUE.equals(JwtUtil.isAdmin()) && !VmsAuthUtils.isVillager()) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "管理员不参与投票");
        }
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (Objects.equals(vote.getStatus(), 1)) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该议题已结束");
        }
        if (vote.getEndTime() != null && vote.getEndTime().before(new Date())) {
            vote.setStatus(1);
            voteMapper.updateById(vote);
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该议题已截止");
        }
        if (agree) {
            vote.setAgreeCount(vote.getAgreeCount() == null ? 1 : vote.getAgreeCount() + 1);
        } else {
            vote.setDisagreeCount(vote.getDisagreeCount() == null ? 1 : vote.getDisagreeCount() + 1);
        }
        voteMapper.updateById(vote);
    }

    @Override
    public void endVote(Long id) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可结束投票");
        }
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        vote.setStatus(1);
        voteMapper.updateById(vote);
    }

    private <T> IPage<T> toModelPage(IPage<Vote> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

