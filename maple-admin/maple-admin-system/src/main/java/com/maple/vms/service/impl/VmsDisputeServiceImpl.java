package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Dispute;
import com.maple.vms.mapper.DisputeMapper;
import com.maple.vms.service.IVmsDisputeService;
import com.maple.vms.vo.model.DisputeModel;
import com.maple.vms.vo.query.DisputePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 矛盾纠纷调解服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsDisputeServiceImpl extends ServiceImpl<DisputeMapper, Dispute> implements IVmsDisputeService {

    private final DisputeMapper disputeMapper;

    @Override
    public IPage<DisputeModel> getPageList(DisputePageQuery query) {
        DisputeModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Dispute> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String partyA = model == null ? null : model.getPartyA();
        String conflictType = model == null ? null : model.getConflictType();
        Integer status = model == null ? null : model.getStatus();
        IPage<Dispute> result = disputeMapper.selectPage(page, Wrappers.lambdaQuery(Dispute.class)
                .like(StringUtils.isNotBlank(partyA), Dispute::getPartyA, partyA)
                .eq(StringUtils.isNotBlank(conflictType), Dispute::getConflictType, conflictType)
                .eq(Objects.nonNull(status), Dispute::getStatus, status)
                .eq(Dispute::getDeleted, 0)
                .orderByDesc(Dispute::getCreateTime));
        return toModelPage(result, DisputeModel.class);
    }

    @Override
    public List<DisputeModel> getList(DisputePageQuery query) {
        DisputeModel model = Objects.isNull(query) ? null : query.getQuery();
        String partyA = model == null ? null : model.getPartyA();
        String conflictType = model == null ? null : model.getConflictType();
        Integer status = model == null ? null : model.getStatus();
        List<Dispute> list = disputeMapper.selectList(Wrappers.lambdaQuery(Dispute.class)
                .like(StringUtils.isNotBlank(partyA), Dispute::getPartyA, partyA)
                .eq(StringUtils.isNotBlank(conflictType), Dispute::getConflictType, conflictType)
                .eq(Objects.nonNull(status), Dispute::getStatus, status)
                .eq(Dispute::getDeleted, 0)
                .orderByDesc(Dispute::getCreateTime));
        return TransformUtils.mapList(list, DisputeModel.class);
    }

    @Override
    public DisputeModel getDisputeById(Long id) {
        return TransformUtils.map(disputeMapper.selectById(id), DisputeModel.class);
    }

    @Override
    public Long createDispute(DisputeModel model) {
        Dispute dispute = TransformUtils.map(model, Dispute.class);
        if (dispute.getDeleted() == null) {
            dispute.setDeleted(0);
        }
        disputeMapper.insert(dispute);
        return dispute.getId();
    }

    @Override
    public void updateDispute(DisputeModel model) {
        disputeMapper.updateById(TransformUtils.map(model, Dispute.class));
    }

    @Override
    public void deleteDispute(Long id) {
        Dispute dispute = disputeMapper.selectById(id);
        if (dispute != null) {
            dispute.setDeleted(1);
            disputeMapper.updateById(dispute);
        }
    }

    private <T> IPage<T> toModelPage(IPage<Dispute> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

