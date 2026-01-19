package com.maple.vms.service.impl;

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
        ApplyModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Apply> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long residentId = model == null ? null : model.getResidentId();
        String applyType = model == null ? null : model.getApplyType();
        Integer status = model == null ? null : model.getStatus();
        IPage<Apply> result = applyMapper.selectPage(page, Wrappers.lambdaQuery(Apply.class)
                .eq(Objects.nonNull(residentId), Apply::getResidentId, residentId)
                .eq(StringUtils.isNotBlank(applyType), Apply::getApplyType, applyType)
                .eq(Objects.nonNull(status), Apply::getStatus, status)
                .eq(VmsAuthUtils.isVillager(), Apply::getResidentId, JwtUtil.getUserId())
                .orderByDesc(Apply::getCreateTime));
        return toModelPage(result, ApplyModel.class);
    }

    @Override
    public ApplyModel getApplyById(Long id) {
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
        applyMapper.insert(apply);
        return apply.getId();
    }

    @Override
    public void updateApply(ApplyModel model) {
        Apply apply = TransformUtils.map(model, Apply.class);
        if (VmsAuthUtils.isVillager()) {
            apply.setResidentId(JwtUtil.getUserId());
        }
        applyMapper.updateById(apply);
    }

    @Override
    public void deleteApply(Long id) {
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
        apply.setStatus(query.getStatus());
        apply.setAuditBy(JwtUtil.getAccount());
        apply.setAuditReply(query.getReply());
        apply.setAuditTime(new Date());
        applyMapper.updateById(apply);
    }

    private <T> IPage<T> toModelPage(IPage<Apply> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

