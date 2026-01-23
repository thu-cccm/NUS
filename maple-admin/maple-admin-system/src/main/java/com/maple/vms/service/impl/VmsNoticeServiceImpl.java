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
import com.maple.vms.bean.Notice;
import com.maple.vms.bean.NoticeRead;
import com.maple.vms.bean.Resident;
import com.maple.vms.mapper.NoticeMapper;
import com.maple.vms.mapper.NoticeReadMapper;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.service.IVmsNoticeService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.NoticeModel;
import com.maple.vms.vo.model.NoticeReadRecordModel;
import com.maple.vms.vo.query.NoticePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通知公告服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsNoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements IVmsNoticeService {

    private final NoticeMapper noticeMapper;
    private final NoticeReadMapper noticeReadMapper;
    private final ResidentMapper residentMapper;

    @Override
    public IPage<NoticeModel> getPageList(NoticePageQuery query) {
        refreshExpiredPolicies();
        NoticeModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Notice> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String title = model == null ? null : model.getTitle();
        String type = model == null ? null : model.getType();
        String targetGroup = model == null ? null : model.getTargetGroup();
        String policyCategory = model == null ? null : model.getPolicyCategory();
        Integer archiveStatus = model == null ? null : model.getArchiveStatus();
        Integer isTop = model == null ? null : model.getIsTop();
        Integer status = model == null ? null : model.getStatus();

        List<String> groupFilter = buildTargetGroupFilter();

        LambdaQueryWrapper<Notice> wrapper = Wrappers.lambdaQuery(Notice.class)
                .and(StringUtils.isNotBlank(title), w -> w.like(Notice::getTitle, title).or().like(Notice::getContent, title))
                .eq(StringUtils.isNotBlank(type), Notice::getType, type)
                .eq(StringUtils.isNotBlank(policyCategory), Notice::getPolicyCategory, policyCategory)
                .eq(Objects.nonNull(archiveStatus), Notice::getArchiveStatus, archiveStatus)
                .eq(Objects.nonNull(isTop), Notice::getIsTop, isTop)
                .eq(Objects.nonNull(status), Notice::getStatus, status)
                .eq(VmsAuthUtils.isVillager(), Notice::getStatus, 1)
                .orderByDesc(Notice::getIsTop)
                .orderByDesc(Notice::getCreateTime);
        applyTargetGroupQuery(wrapper, targetGroup);
        applyGroupFilter(wrapper, groupFilter, VmsAuthUtils.isVillager());
        IPage<Notice> result = noticeMapper.selectPage(page, wrapper);
        return enrichNoticePage(result);
    }

    @Override
    public NoticeModel getNoticeById(Long id) {
        refreshExpiredPolicies();
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            return null;
        }
        if (VmsAuthUtils.isVillager()) {
            if (!Objects.equals(notice.getStatus(), 1)) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该公告");
            }
            List<String> groupFilter = buildTargetGroupFilter();
            if (!isTargetGroupAllowed(notice.getTargetGroup(), groupFilter)) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该公告");
            }
        }
        NoticeModel model = TransformUtils.map(notice, NoticeModel.class);
        if (Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            fillReadStats(model);
        } else if (VmsAuthUtils.isVillager()) {
            model.setReadStatus(hasRead(notice.getId(), JwtUtil.getUserId()));
        }
        model.setExpired(isExpired(notice));
        return model;
    }

    @Override
    public Long createNotice(NoticeModel model) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可发布公告");
        }
        Notice notice = TransformUtils.map(model, Notice.class);
        if (!"政策".equals(notice.getType())) {
            notice.setPolicyFile(null);
            notice.setPolicyCategory(null);
            notice.setExpireTime(null);
            notice.setArchiveStatus(0);
        }
        if (isExpired(notice)) {
            notice.setArchiveStatus(1);
        }
        if (notice.getIsTop() == null) {
            notice.setIsTop(0);
        }
        if (notice.getStatus() == null) {
            notice.setStatus(1);
        }
        if (notice.getArchiveStatus() == null) {
            notice.setArchiveStatus(0);
        }
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(NoticeModel model) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可修改公告");
        }
        Notice notice = TransformUtils.map(model, Notice.class);
        if (!"政策".equals(notice.getType())) {
            notice.setPolicyFile(null);
            notice.setPolicyCategory(null);
            notice.setExpireTime(null);
            notice.setArchiveStatus(0);
        }
        if (isExpired(notice)) {
            notice.setArchiveStatus(1);
        }
        if (notice.getArchiveStatus() == null) {
            notice.setArchiveStatus(0);
        }
        noticeMapper.updateById(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可删除公告");
        }
        noticeMapper.deleteById(id);
    }

    @Override
    public List<NoticeModel> getTopList(int limit) {
        refreshExpiredPolicies();
        List<String> groupFilter = buildTargetGroupFilter();
        LambdaQueryWrapper<Notice> wrapper = Wrappers.lambdaQuery(Notice.class)
                .eq(Notice::getStatus, 1)
                .eq(Notice::getArchiveStatus, 0)
                .orderByDesc(Notice::getIsTop)
                .orderByDesc(Notice::getCreateTime)
                .last("limit " + limit);
        applyGroupFilter(wrapper, groupFilter, VmsAuthUtils.isVillager());
        List<Notice> list = noticeMapper.selectList(wrapper);
        List<NoticeModel> models = TransformUtils.mapList(list, NoticeModel.class);
        if (VmsAuthUtils.isVillager()) {
            fillReadStatus(models, JwtUtil.getUserId());
        }
        models.forEach(model -> model.setExpired(isExpired(TransformUtils.map(model, Notice.class))));
        return models;
    }

    @Override
    public void markRead(Long id) {
        if (!VmsAuthUtils.isVillager()) {
            return;
        }
        if (id == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Notice notice = noticeMapper.selectById(id);
        if (notice == null || !Objects.equals(notice.getStatus(), 1)) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        List<String> groupFilter = buildTargetGroupFilter();
        if (!isTargetGroupAllowed(notice.getTargetGroup(), groupFilter)) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该公告");
        }
        Long residentId = JwtUtil.getUserId();
        if (residentId == null) {
            return;
        }
        Long count = noticeReadMapper.selectCount(Wrappers.lambdaQuery(NoticeRead.class)
                .eq(NoticeRead::getNoticeId, id)
                .eq(NoticeRead::getResidentId, residentId));
        if (count != null && count > 0) {
            return;
        }
        NoticeRead record = new NoticeRead();
        record.setNoticeId(id);
        record.setResidentId(residentId);
        record.setReadTime(new Date());
        noticeReadMapper.insert(record);
    }

    @Override
    public void archivePolicy(Long id, Integer archiveStatus) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可归档政策");
        }
        if (id == null || archiveStatus == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (!"政策".equals(notice.getType())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅政策可归档");
        }
        notice.setArchiveStatus(archiveStatus);
        noticeMapper.updateById(notice);
    }

    @Override
    public void batchUpdateExpireTime(List<Long> ids, Date expireTime) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可批量设置过期时间");
        }
        if (ids == null || ids.isEmpty()) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        if (expireTime == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        noticeMapper.update(null, new UpdateWrapper<Notice>()
                .set("expire_time", expireTime)
                .set("archive_status", 0)
                .eq("type", "政策")
                .in("id", ids));
        // If expire time is already past, mark archived
        if (expireTime.before(new Date())) {
            noticeMapper.update(null, new UpdateWrapper<Notice>()
                    .set("archive_status", 1)
                    .eq("type", "政策")
                    .in("id", ids));
        }
    }

    @Override
    public List<NoticeReadRecordModel> getReadRecords(Long id, String keyword) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            return List.of();
        }
        if (id == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        List<NoticeRead> reads = noticeReadMapper.selectList(Wrappers.lambdaQuery(NoticeRead.class)
                .eq(NoticeRead::getNoticeId, id)
                .orderByDesc(NoticeRead::getReadTime));
        if (reads.isEmpty()) {
            return List.of();
        }
        List<Long> residentIds = reads.stream()
                .map(NoticeRead::getResidentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Resident> residents = residentMapper.selectBatchIds(residentIds);
        java.util.Map<Long, String> nameMap = residents.stream()
                .collect(Collectors.toMap(Resident::getId, Resident::getRealName, (a, b) -> a));
        return reads.stream()
                .map(record -> new NoticeReadRecordModel(
                        record.getResidentId(),
                        nameMap.getOrDefault(record.getResidentId(), "未知"),
                        record.getReadTime()))
                .filter(model -> StringUtils.isBlank(keyword) || model.getResidentName().contains(keyword))
                .collect(Collectors.toList());
    }

    private IPage<NoticeModel> enrichNoticePage(IPage<Notice> page) {
        IPage<NoticeModel> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<NoticeModel> models = TransformUtils.mapList(page.getRecords(), NoticeModel.class);
        if (Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            models.forEach(this::fillReadStats);
        } else if (VmsAuthUtils.isVillager()) {
            fillReadStatus(models, JwtUtil.getUserId());
        }
        models.forEach(model -> model.setExpired(isExpired(TransformUtils.map(model, Notice.class))));
        result.setRecords(models);
        return result;
    }

    private void fillReadStats(NoticeModel model) {
        if (model == null || model.getId() == null) {
            return;
        }
        TargetGroupResult target = resolveTargetResidents(model.getTargetGroup());
        model.setTargetCount(target.totalCount);
        if (target.allResidents) {
            Long readCount = noticeReadMapper.selectCount(Wrappers.lambdaQuery(NoticeRead.class)
                    .eq(NoticeRead::getNoticeId, model.getId()));
            model.setReadCount(readCount == null ? 0L : readCount);
        } else if (!target.residentIds.isEmpty()) {
            Long readCount = noticeReadMapper.selectCount(Wrappers.lambdaQuery(NoticeRead.class)
                    .eq(NoticeRead::getNoticeId, model.getId())
                    .in(NoticeRead::getResidentId, target.residentIds));
            model.setReadCount(readCount == null ? 0L : readCount);
        } else {
            model.setReadCount(0L);
        }
    }

    private boolean isExpired(Notice notice) {
        if (notice == null || notice.getExpireTime() == null) {
            return false;
        }
        return notice.getExpireTime().before(new Date());
    }

    private void refreshExpiredPolicies() {
        noticeMapper.update(null, new UpdateWrapper<Notice>()
                .set("archive_status", 1)
                .eq("type", "政策")
                .eq("archive_status", 0)
                .isNotNull("expire_time")
                .lt("expire_time", new Date()));
    }

    private void fillReadStatus(List<NoticeModel> models, Long residentId) {
        if (residentId == null || models == null || models.isEmpty()) {
            return;
        }
        List<Long> noticeIds = models.stream()
                .map(NoticeModel::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (noticeIds.isEmpty()) {
            return;
        }
        Set<Long> readIds = noticeReadMapper.selectList(Wrappers.lambdaQuery(NoticeRead.class)
                        .eq(NoticeRead::getResidentId, residentId)
                        .in(NoticeRead::getNoticeId, noticeIds))
                .stream()
                .map(NoticeRead::getNoticeId)
                .collect(Collectors.toSet());
        models.forEach(model -> model.setReadStatus(readIds.contains(model.getId())));
    }

    private boolean hasRead(Long noticeId, Long residentId) {
        if (noticeId == null || residentId == null) {
            return false;
        }
        Long count = noticeReadMapper.selectCount(Wrappers.lambdaQuery(NoticeRead.class)
                .eq(NoticeRead::getNoticeId, noticeId)
                .eq(NoticeRead::getResidentId, residentId));
        return count != null && count > 0;
    }

    private List<String> buildTargetGroupFilter() {
        List<String> groups = new ArrayList<>();
        groups.add("all");
        if (!VmsAuthUtils.isVillager()) {
            return groups;
        }
        Resident resident = residentMapper.selectById(JwtUtil.getUserId());
        if (resident != null) {
            if ("党员".equals(resident.getPolitics())) {
                groups.add("party");
            }
            if (Objects.equals(resident.getIsPoor(), 1)) {
                groups.add("poor");
            }
        }
        return groups;
    }

    private void applyTargetGroupQuery(LambdaQueryWrapper<Notice> wrapper, String targetGroup) {
        if (StringUtils.isBlank(targetGroup)) {
            return;
        }
        wrapper.apply("FIND_IN_SET({0}, target_group)", targetGroup);
    }

    private void applyGroupFilter(LambdaQueryWrapper<Notice> wrapper, List<String> groupFilter, boolean enabled) {
        if (!enabled || groupFilter == null || groupFilter.isEmpty()) {
            return;
        }
        wrapper.and(true, w -> {
            for (int i = 0; i < groupFilter.size(); i++) {
                if (i == 0) {
                    w.apply("FIND_IN_SET({0}, target_group)", groupFilter.get(i));
                } else {
                    w.or().apply("FIND_IN_SET({0}, target_group)", groupFilter.get(i));
                }
            }
        });
    }

    private boolean isTargetGroupAllowed(String targetGroup, List<String> groupFilter) {
        if (StringUtils.isBlank(targetGroup) || groupFilter == null || groupFilter.isEmpty()) {
            return false;
        }
        String[] parts = targetGroup.split(",");
        for (String group : groupFilter) {
            for (String part : parts) {
                if (group.equals(part)) {
                    return true;
                }
            }
        }
        return false;
    }

    private TargetGroupResult resolveTargetResidents(String targetGroup) {
        TargetGroupResult result = new TargetGroupResult();
        if (StringUtils.isBlank(targetGroup)) {
            result.totalCount = 0L;
            return result;
        }
        Set<String> groups = parseTargetGroups(targetGroup);
        if (groups.contains("all")) {
            result.allResidents = true;
            result.totalCount = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class));
            return result;
        }
        Set<Long> residentIds = new HashSet<>();
        if (groups.contains("party")) {
            residentIds.addAll(residentMapper.selectList(Wrappers.lambdaQuery(Resident.class)
                            .eq(Resident::getPolitics, "党员"))
                    .stream()
                    .map(Resident::getId)
                    .collect(Collectors.toSet()));
        }
        if (groups.contains("poor")) {
            residentIds.addAll(residentMapper.selectList(Wrappers.lambdaQuery(Resident.class)
                            .eq(Resident::getIsPoor, 1))
                    .stream()
                    .map(Resident::getId)
                    .collect(Collectors.toSet()));
        }
        result.residentIds = residentIds;
        result.totalCount = (long) residentIds.size();
        return result;
    }

    private Set<String> parseTargetGroups(String targetGroup) {
        Set<String> groups = new HashSet<>();
        String[] parts = targetGroup.split(",");
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                groups.add(part);
            }
        }
        return groups;
    }

    private static class TargetGroupResult {
        private boolean allResidents = false;
        private Set<Long> residentIds = new HashSet<>();
        private Long totalCount = 0L;
    }
}

