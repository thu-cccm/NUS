package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Vote;
import com.maple.vms.bean.Resident;
import com.maple.vms.bean.VoteRecord;
import com.maple.vms.mapper.VoteMapper;
import com.maple.vms.mapper.VoteRecordMapper;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.service.IVmsVoteService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.VoteModel;
import com.maple.vms.vo.model.VoteRecordModel;
import com.maple.vms.vo.model.VoteRecordPageResult;
import com.maple.vms.vo.model.VoteTrendModel;
import com.maple.vms.vo.query.VotePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 民主互动服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsVoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements IVmsVoteService {

    private final VoteMapper voteMapper;
    private final VoteRecordMapper voteRecordMapper;
    private final ResidentMapper residentMapper;

    @Override
    public IPage<VoteModel> getPageList(VotePageQuery query) {
        refreshExpiredVotes();
        VoteModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Vote> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String title = model == null ? null : model.getTitle();
        Integer status = model == null ? null : model.getStatus();
        IPage<Vote> result = voteMapper.selectPage(page, Wrappers.lambdaQuery(Vote.class)
                .like(StringUtils.isNotBlank(title), Vote::getTitle, title)
                .eq(Objects.nonNull(status), Vote::getStatus, status)
                .orderByDesc(Vote::getId));
        return toModelPage(result);
    }

    @Override
    public VoteModel getVoteById(Long id) {
        refreshExpiredVotes();
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            return null;
        }
        VoteModel model = TransformUtils.map(vote, VoteModel.class);
        if (VmsAuthUtils.isVillager()) {
            model.setVoted(hasVoted(id, JwtUtil.getUserId()));
        }
        return model;
    }

    @Override
    public Long createVote(VoteModel model) {
        Vote vote = TransformUtils.map(model, Vote.class);
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可发布投票");
        }
        if (vote.getAgreeCount() == null) {
            vote.setAgreeCount(0);
        }
        if (vote.getDisagreeCount() == null) {
            vote.setDisagreeCount(0);
        }
        if (vote.getIsAnonymous() == null) {
            vote.setIsAnonymous(0);
        }
        if (vote.getStatus() == null) {
            vote.setStatus(0);
        }
        voteMapper.insert(vote);
        return vote.getId();
    }

    @Override
    public void updateVote(VoteModel model) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可修改投票");
        }
        voteMapper.updateById(TransformUtils.map(model, Vote.class));
    }

    @Override
    public void deleteVote(Long id) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可删除投票");
        }
        voteMapper.deleteById(id);
    }

    @Override
    public void castVote(Long id, boolean agree) {
        if (Boolean.TRUE.equals(JwtUtil.isAdmin()) && !VmsAuthUtils.isVillager()) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "管理员不参与投票");
        }
        refreshExpiredVotes();
        if (hasVoted(id, JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该议题已投票");
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
        VoteRecord record = new VoteRecord();
        record.setVoteId(id);
        record.setResidentId(JwtUtil.getUserId());
        record.setAgree(agree);
        voteRecordMapper.insert(record);
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

    private IPage<VoteModel> toModelPage(IPage<Vote> page) {
        IPage<VoteModel> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<VoteModel> models = TransformUtils.mapList(page.getRecords(), VoteModel.class);
        if (VmsAuthUtils.isVillager() && !models.isEmpty()) {
            List<Long> voteIds = models.stream().map(VoteModel::getId).collect(Collectors.toList());
            Set<Long> votedIds = voteRecordMapper.selectList(Wrappers.lambdaQuery(VoteRecord.class)
                            .eq(VoteRecord::getResidentId, JwtUtil.getUserId())
                            .in(VoteRecord::getVoteId, voteIds))
                    .stream()
                    .map(VoteRecord::getVoteId)
                    .collect(Collectors.toSet());
            models.forEach(model -> model.setVoted(votedIds.contains(model.getId())));
        }
        result.setRecords(models);
        return result;
    }

    private boolean hasVoted(Long voteId, Long residentId) {
        if (voteId == null || residentId == null) {
            return false;
        }
        return voteRecordMapper.selectCount(Wrappers.lambdaQuery(VoteRecord.class)
                .eq(VoteRecord::getVoteId, voteId)
                .eq(VoteRecord::getResidentId, residentId)) > 0;
    }

    @Override
    public List<VoteRecordModel> getVoteRecords(Long id, Boolean agree, String keyword) {
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            return List.of();
        }
        RecordQueryContext ctx = buildRecordQuery(id, vote, agree, keyword);
        if (!ctx.available) {
            return List.of();
        }
        List<VoteRecord> records = voteRecordMapper.selectList(ctx.wrapper.orderByDesc(VoteRecord::getCreateTime));
        return toRecordModels(records, ctx.isAnonymous);
    }

    @Override
    public VoteRecordPageResult getVoteRecordPage(Long id, int current, int size, Boolean agree, String keyword) {
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        VoteRecordPageResult result = new VoteRecordPageResult();
        result.setCurrent(current);
        result.setSize(size);
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            result.setTotal(0);
            result.setAgreeCount(0);
            result.setDisagreeCount(0);
            result.setRecords(List.of());
            return result;
        }
        RecordQueryContext ctx = buildRecordQuery(id, vote, agree, keyword);
        if (!ctx.available) {
            result.setTotal(0);
            result.setAgreeCount(0);
            result.setDisagreeCount(0);
            result.setRecords(List.of());
            return result;
        }
        Page<VoteRecord> page = new Page<>(current, size);
        IPage<VoteRecord> pageResult = voteRecordMapper.selectPage(page, ctx.wrapper.orderByDesc(VoteRecord::getCreateTime));
        result.setTotal(pageResult.getTotal());
        result.setRecords(toRecordModels(pageResult.getRecords(), ctx.isAnonymous));

        Map<String, Object> summary = voteRecordMapper.selectMaps(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<VoteRecord>()
                        .select("sum(case when agree = 1 then 1 else 0 end) as agree_cnt",
                                "sum(case when agree = 0 then 1 else 0 end) as disagree_cnt")
                        .eq("vote_id", id)
                        .eq(Objects.nonNull(agree), "agree", agree)
                        .in(ctx.residentIds != null, "resident_id", ctx.residentIds))
                .stream()
                .findFirst()
                .orElse(new HashMap<>());
        result.setAgreeCount(toLong(summary.get("agree_cnt")));
        result.setDisagreeCount(toLong(summary.get("disagree_cnt")));
        return result;
    }

    @Override
    public List<VoteTrendModel> getVoteTrend(Long id, Integer days, String unit) {
        Vote vote = voteMapper.selectById(id);
        if (vote == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        int rangeDays = days == null || days <= 0 ? 7 : days;
        Date startDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(rangeDays));
        String format = "%m-%d";
        if ("week".equalsIgnoreCase(unit)) {
            format = "%Y-%u";
        } else if ("month".equalsIgnoreCase(unit)) {
            format = "%Y-%m";
        }
        List<Map<String, Object>> stats = voteRecordMapper.selectMaps(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<VoteRecord>()
                .select(String.format("date_format(create_time,'%s') as day", format),
                        "sum(case when agree = 1 then 1 else 0 end) as agree_cnt",
                        "sum(case when agree = 0 then 1 else 0 end) as disagree_cnt")
                .eq("vote_id", id)
                .ge("create_time", startDate)
                .groupBy("day")
                .orderByAsc("day"));
        return stats.stream()
                .map(item -> new VoteTrendModel(
                        String.valueOf(item.get("day")),
                        toLong(item.get("agree_cnt")),
                        toLong(item.get("disagree_cnt"))
                ))
                .collect(Collectors.toList());
    }

    private void refreshExpiredVotes() {
        voteMapper.update(null, new UpdateWrapper<Vote>()
                .set("status", 1)
                .eq("status", 0)
                .isNotNull("end_time")
                .lt("end_time", new Date()));
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(String.valueOf(value));
    }

    private RecordQueryContext buildRecordQuery(Long id, Vote vote, Boolean agree, String keyword) {
        RecordQueryContext ctx = new RecordQueryContext();
        ctx.isAnonymous = Objects.equals(vote.getIsAnonymous(), 1);
        if (StringUtils.isNotBlank(keyword) && ctx.isAnonymous) {
            ctx.available = false;
            return ctx;
        }
        List<Long> residentIds = null;
        if (StringUtils.isNotBlank(keyword)) {
            residentIds = residentMapper.selectList(Wrappers.lambdaQuery(Resident.class)
                            .like(Resident::getRealName, keyword))
                    .stream()
                    .map(Resident::getId)
                    .collect(Collectors.toList());
            if (residentIds.isEmpty()) {
                ctx.available = false;
                return ctx;
            }
        }
        ctx.residentIds = residentIds;
        ctx.wrapper = Wrappers.lambdaQuery(VoteRecord.class)
                .eq(VoteRecord::getVoteId, id)
                .eq(Objects.nonNull(agree), VoteRecord::getAgree, agree)
                .in(residentIds != null, VoteRecord::getResidentId, residentIds);
        ctx.available = true;
        return ctx;
    }

    private List<VoteRecordModel> toRecordModels(List<VoteRecord> records, boolean isAnonymous) {
        if (records == null || records.isEmpty()) {
            return List.of();
        }
        Map<Long, String> nameMap = new HashMap<>();
        if (!isAnonymous) {
            List<Long> recordResidentIds = records.stream()
                    .map(VoteRecord::getResidentId)
                    .distinct()
                    .collect(Collectors.toList());
            List<Resident> residents = residentMapper.selectBatchIds(recordResidentIds);
            nameMap = residents.stream().collect(Collectors.toMap(Resident::getId, Resident::getRealName, (a, b) -> a));
        }
        Map<Long, String> finalNameMap = nameMap;
        return records.stream()
                .map(record -> new VoteRecordModel(
                        isAnonymous ? null : record.getResidentId(),
                        isAnonymous ? "匿名" : finalNameMap.getOrDefault(record.getResidentId(), ""),
                        record.getAgree(),
                        record.getCreateTime()))
                .collect(Collectors.toList());
    }

    private static class RecordQueryContext {
        private boolean available;
        private boolean isAnonymous;
        private List<Long> residentIds;
        private LambdaQueryWrapper<VoteRecord> wrapper;
    }
}

