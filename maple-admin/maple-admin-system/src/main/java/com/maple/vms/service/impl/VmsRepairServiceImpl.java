package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Repair;
import com.maple.vms.mapper.RepairMapper;
import com.maple.vms.service.IVmsRepairService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.RepairModel;
import com.maple.vms.vo.query.RepairPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 设施报修服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsRepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements IVmsRepairService {

    private final RepairMapper repairMapper;

    @Override
    public IPage<RepairModel> getPageList(RepairPageQuery query) {
        RepairModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Repair> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String facilityName = model == null ? null : model.getFacilityName();
        String facilityType = model == null ? null : model.getFacilityType();
        Integer status = model == null ? null : model.getStatus();
        IPage<Repair> result = repairMapper.selectPage(page, Wrappers.lambdaQuery(Repair.class)
                .like(StringUtils.isNotBlank(facilityName), Repair::getFacilityName, facilityName)
                .eq(StringUtils.isNotBlank(facilityType), Repair::getFacilityType, facilityType)
                .eq(Objects.nonNull(status), Repair::getStatus, status)
                .and(VmsAuthUtils.isVillager(), w -> w.eq(Repair::getReporterId, JwtUtil.getUserId()))
                .eq(Repair::getDeleted, 0)
                .orderByDesc(Repair::getCreateTime));
        return toModelPage(result, RepairModel.class);
    }

    @Override
    public List<RepairModel> getList(RepairPageQuery query) {
        RepairModel model = Objects.isNull(query) ? null : query.getQuery();
        String facilityName = model == null ? null : model.getFacilityName();
        String facilityType = model == null ? null : model.getFacilityType();
        Integer status = model == null ? null : model.getStatus();
        List<Repair> list = repairMapper.selectList(Wrappers.lambdaQuery(Repair.class)
                .like(StringUtils.isNotBlank(facilityName), Repair::getFacilityName, facilityName)
                .eq(StringUtils.isNotBlank(facilityType), Repair::getFacilityType, facilityType)
                .eq(Objects.nonNull(status), Repair::getStatus, status)
                .and(VmsAuthUtils.isVillager(), w -> w.eq(Repair::getReporterId, JwtUtil.getUserId()))
                .eq(Repair::getDeleted, 0)
                .orderByDesc(Repair::getCreateTime));
        return TransformUtils.mapList(list, RepairModel.class);
    }

    @Override
    public RepairModel getRepairById(Long id) {
        return TransformUtils.map(repairMapper.selectById(id), RepairModel.class);
    }

    @Override
    public Long createRepair(RepairModel model) {
        Repair repair = TransformUtils.map(model, Repair.class);
        if (repair.getDeleted() == null) {
            repair.setDeleted(0);
        }
        if (repair.getStatus() == null) {
            repair.setStatus(0);
        }
        if (VmsAuthUtils.isVillager()) {
            repair.setReporterId(JwtUtil.getUserId());
        }
        repairMapper.insert(repair);
        return repair.getId();
    }

    @Override
    public void updateRepair(RepairModel model) {
        repairMapper.updateById(TransformUtils.map(model, Repair.class));
    }

    @Override
    public void deleteRepair(Long id) {
        Repair repair = repairMapper.selectById(id);
        if (repair != null) {
            repair.setDeleted(1);
            repairMapper.updateById(repair);
        }
    }

    @Override
    public void assignRepair(Long id, String assignee) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            throw new RuntimeException("报修记录不存在");
        }
        repair.setStatus(1);
        repair.setAssignee(assignee);
        repair.setAssignTime(new Date());
        repairMapper.updateById(repair);
    }

    @Override
    public void completeRepair(Long id, String repairResult) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            throw new RuntimeException("报修记录不存在");
        }
        repair.setStatus(2);
        repair.setRepairResult(repairResult);
        repair.setCompleteTime(new Date());
        repairMapper.updateById(repair);
    }

    private <T> IPage<T> toModelPage(IPage<Repair> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

