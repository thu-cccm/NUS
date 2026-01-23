package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Apply;
import com.maple.vms.mapper.ApplyMapper;
import com.maple.vms.service.IVmsApplyService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.ApplyModel;
import com.maple.vms.vo.query.ApplyAuditQuery;
import com.maple.vms.vo.query.ApplyPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 事务申请服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements IVmsApplyService {

    private final ApplyMapper applyMapper;

    @Override
    public IPage<ApplyModel> getPageList(ApplyPageQuery query) {
        refreshPublicStatus();
        ApplyModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Apply> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long residentId = model == null ? null : model.getResidentId();
        String applyType = model == null ? null : model.getApplyType();
        Integer status = model == null ? null : model.getStatus();
        Integer publicStatus = model == null ? null : model.getPublicStatus();
        Integer archiveStatus = model == null ? null : model.getArchiveStatus();
        IPage<Apply> result = applyMapper.selectPage(page, Wrappers.lambdaQuery(Apply.class)
                .eq(Objects.nonNull(residentId), Apply::getResidentId, residentId)
                .eq(StringUtils.isNotBlank(applyType), Apply::getApplyType, applyType)
                .eq(Objects.nonNull(status), Apply::getStatus, status)
                .eq(Objects.nonNull(publicStatus), Apply::getPublicStatus, publicStatus)
                .eq(Objects.nonNull(archiveStatus), Apply::getArchiveStatus, archiveStatus)
                .eq(VmsAuthUtils.isVillager(), Apply::getResidentId, JwtUtil.getUserId())
                .orderByDesc(Apply::getCreateTime));
        return toModelPage(result, ApplyModel.class);
    }

    @Override
    public List<ApplyModel> getList(ApplyPageQuery query) {
        refreshPublicStatus();
        ApplyModel model = Objects.isNull(query) ? null : query.getQuery();
        Long residentId = model == null ? null : model.getResidentId();
        String applyType = model == null ? null : model.getApplyType();
        Integer status = model == null ? null : model.getStatus();
        Integer publicStatus = model == null ? null : model.getPublicStatus();
        Integer archiveStatus = model == null ? null : model.getArchiveStatus();
        List<Apply> list = applyMapper.selectList(Wrappers.lambdaQuery(Apply.class)
                .eq(Objects.nonNull(residentId), Apply::getResidentId, residentId)
                .eq(StringUtils.isNotBlank(applyType), Apply::getApplyType, applyType)
                .eq(Objects.nonNull(status), Apply::getStatus, status)
                .eq(Objects.nonNull(publicStatus), Apply::getPublicStatus, publicStatus)
                .eq(Objects.nonNull(archiveStatus), Apply::getArchiveStatus, archiveStatus)
                .eq(VmsAuthUtils.isVillager(), Apply::getResidentId, JwtUtil.getUserId())
                .orderByDesc(Apply::getCreateTime));
        return TransformUtils.mapList(list, ApplyModel.class);
    }

    @Override
    public ApplyModel getApplyById(Long id) {
        refreshPublicStatus();
        Apply apply = applyMapper.selectById(id);
        if (apply == null) {
            return null;
        }
        if (VmsAuthUtils.isVillager() && !Objects.equals(apply.getResidentId(), JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该申请信息");
        }
        return TransformUtils.map(apply, ApplyModel.class);
    }

    @Override
    public Long createApply(ApplyModel model) {
        Apply apply = TransformUtils.map(model, Apply.class);
        if (VmsAuthUtils.isVillager()) {
            apply.setResidentId(JwtUtil.getUserId());
        }
        if (apply.getStatus() == null) {
            apply.setStatus(0);
        }
        if (apply.getPublicStatus() == null) {
            apply.setPublicStatus(0);
        }
        if (apply.getArchiveStatus() == null) {
            apply.setArchiveStatus(0);
        }
        applyMapper.insert(apply);
        return apply.getId();
    }

    @Override
    public void updateApply(ApplyModel model) {
        Apply apply = TransformUtils.map(model, Apply.class);
        if (VmsAuthUtils.isVillager()) {
            Apply current = applyMapper.selectById(model.getId());
            if (current == null) {
                throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
            }
            if (!Objects.equals(current.getResidentId(), JwtUtil.getUserId())) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限修改该申请");
            }
            if (!Objects.equals(current.getStatus(), 2)) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅驳回申请可修改");
            }
            apply.setResidentId(JwtUtil.getUserId());
            apply.setStatus(current.getStatus());
            apply.setAuditBy(current.getAuditBy());
            apply.setAuditReply(current.getAuditReply());
            apply.setAuditTime(current.getAuditTime());
            apply.setPublicStatus(current.getPublicStatus());
            apply.setPublicStart(current.getPublicStart());
            apply.setPublicEnd(current.getPublicEnd());
            apply.setArchiveStatus(current.getArchiveStatus());
            apply.setArchiveTime(current.getArchiveTime());
        }
        if (apply.getPublicStatus() == null) {
            apply.setPublicStatus(0);
        }
        if (apply.getArchiveStatus() == null) {
            apply.setArchiveStatus(0);
        }
        applyMapper.updateById(apply);
    }

    @Override
    public void resubmitApply(ApplyModel model) {
        if (!VmsAuthUtils.isVillager()) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅村民可重新提交");
        }
        if (model == null || model.getId() == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Apply current = applyMapper.selectById(model.getId());
        if (current == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (!Objects.equals(current.getResidentId(), JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限操作该申请");
        }
        if (!Objects.equals(current.getStatus(), 2)) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅驳回申请可重新提交");
        }
        current.setApplyType(model.getApplyType());
        current.setContent(model.getContent());
        current.setProofFiles(model.getProofFiles());
        current.setStatus(0);
        current.setAuditBy(null);
        current.setAuditReply(null);
        current.setAuditTime(null);
        current.setPublicStatus(0);
        current.setPublicStart(null);
        current.setPublicEnd(null);
        current.setArchiveStatus(0);
        current.setArchiveTime(null);
        applyMapper.updateById(current);
    }

    @Override
    public void archiveApply(Long id, Integer archiveStatus) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可归档申请");
        }
        if (id == null || archiveStatus == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Apply apply = applyMapper.selectById(id);
        if (apply == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (Objects.equals(archiveStatus, 1) && Objects.equals(apply.getStatus(), 0)) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "待审核申请不可归档");
        }
        apply.setArchiveStatus(archiveStatus);
        apply.setArchiveTime(Objects.equals(archiveStatus, 1) ? new Date() : null);
        applyMapper.updateById(apply);
    }

    @Override
    public void deleteApply(Long id) {
        if (VmsAuthUtils.isVillager()) {
            Apply apply = applyMapper.selectById(id);
            if (apply == null) {
                throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
            }
            if (!Objects.equals(apply.getResidentId(), JwtUtil.getUserId())) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限删除该申请");
            }
            if (Objects.equals(apply.getArchiveStatus(), 1)) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "已归档申请不可删除");
            }
            if (Objects.equals(apply.getStatus(), 1)) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "已通过申请不可删除");
            }
        }
        applyMapper.deleteById(id);
    }

    @Override
    public void auditApply(ApplyAuditQuery query) {
        if (!Boolean.TRUE.equals(JwtUtil.isAdmin())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "仅管理员可审核申请");
        }
        if (query == null || query.getApplyId() == null || query.getStatus() == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        Apply apply = applyMapper.selectById(query.getApplyId());
        if (apply == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (!Objects.equals(query.getStatus(), 1) && !Objects.equals(query.getStatus(), 2)) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        if (Objects.equals(query.getStatus(), 2) && StringUtils.isBlank(query.getReply())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "驳回时必须填写驳回原因");
        }
        apply.setStatus(query.getStatus());
        apply.setAuditBy(JwtUtil.getAccount());
        apply.setAuditReply(query.getReply());
        apply.setAuditTime(new Date());
        if (Objects.equals(query.getStatus(), 1)) {
            Date now = new Date();
            Date end = query.getPublicEndTime() == null ? new Date(now.getTime() + 7L * 24 * 60 * 60 * 1000) : new Date(query.getPublicEndTime());
            apply.setPublicStatus(1);
            apply.setPublicStart(now);
            apply.setPublicEnd(end);
            apply.setArchiveStatus(0);
            apply.setArchiveTime(null);
        } else {
            apply.setPublicStatus(0);
            apply.setPublicStart(null);
            apply.setPublicEnd(null);
        }
        applyMapper.updateById(apply);
    }

    private void refreshPublicStatus() {
        Date now = new Date();
        applyMapper.update(null, new UpdateWrapper<Apply>()
                .set("public_status", 2)
                .set("archive_status", 1)
                .set("archive_time", now)
                .eq("status", 1)
                .eq("public_status", 1)
                .isNotNull("public_end")
                .lt("public_end", now));
    }

    private <T> IPage<T> toModelPage(IPage<Apply> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

