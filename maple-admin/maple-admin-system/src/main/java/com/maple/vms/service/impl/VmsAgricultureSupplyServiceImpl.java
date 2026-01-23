package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.AgricultureSupply;
import com.maple.vms.mapper.AgricultureSupplyMapper;
import com.maple.vms.service.IVmsAgricultureSupplyService;
import com.maple.vms.vo.model.AgricultureSupplyModel;
import com.maple.vms.vo.query.AgricultureSupplyPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 农资发放记录服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsAgricultureSupplyServiceImpl extends ServiceImpl<AgricultureSupplyMapper, AgricultureSupply> implements IVmsAgricultureSupplyService {

    private final AgricultureSupplyMapper supplyMapper;

    @Override
    public IPage<AgricultureSupplyModel> getPageList(AgricultureSupplyPageQuery query) {
        AgricultureSupplyModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<AgricultureSupply> page = new Page<>();
        if (query != null && query.getPage() != null) {
            page.setCurrent(query.getPage().getCurrent());
            page.setSize(query.getPage().getSize());
        }
        String recipientName = model == null ? null : model.getRecipientName();
        String supplyType = model == null ? null : model.getSupplyType();
        IPage<AgricultureSupply> result = supplyMapper.selectPage(page, Wrappers.lambdaQuery(AgricultureSupply.class)
                .like(StringUtils.isNotBlank(recipientName), AgricultureSupply::getRecipientName, recipientName)
                .eq(StringUtils.isNotBlank(supplyType), AgricultureSupply::getSupplyType, supplyType)
                .eq(AgricultureSupply::getDeleted, 0)
                .orderByDesc(AgricultureSupply::getIssueDate)
                .orderByDesc(AgricultureSupply::getId));
        return toModelPage(result, AgricultureSupplyModel.class);
    }

    @Override
    public List<AgricultureSupplyModel> getList(AgricultureSupplyPageQuery query) {
        AgricultureSupplyModel model = Objects.isNull(query) ? null : query.getQuery();
        String recipientName = model == null ? null : model.getRecipientName();
        String supplyType = model == null ? null : model.getSupplyType();
        List<AgricultureSupply> list = supplyMapper.selectList(Wrappers.lambdaQuery(AgricultureSupply.class)
                .like(StringUtils.isNotBlank(recipientName), AgricultureSupply::getRecipientName, recipientName)
                .eq(StringUtils.isNotBlank(supplyType), AgricultureSupply::getSupplyType, supplyType)
                .eq(AgricultureSupply::getDeleted, 0)
                .orderByDesc(AgricultureSupply::getIssueDate)
                .orderByDesc(AgricultureSupply::getId));
        return TransformUtils.mapList(list, AgricultureSupplyModel.class);
    }

    @Override
    public AgricultureSupplyModel getAgricultureSupplyById(Long id) {
        if (id == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        AgricultureSupply supply = supplyMapper.selectById(id);
        if (supply == null || supply.getDeleted() == 1) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        return TransformUtils.map(supply, AgricultureSupplyModel.class);
    }

    @Override
    public Long createAgricultureSupply(AgricultureSupplyModel model) {
        if (model == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        if (StringUtils.isBlank(model.getRecipientName())) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "领取人姓名不能为空");
        }
        if (StringUtils.isBlank(model.getSupplyName())) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "物资名称不能为空");
        }
        AgricultureSupply supply = TransformUtils.map(model, AgricultureSupply.class);
        supply.setDeleted(0);
        if (supply.getUnit() == null) {
            supply.setUnit("公斤");
        }
        supplyMapper.insert(supply);
        return supply.getId();
    }

    @Override
    public void updateAgricultureSupply(AgricultureSupplyModel model) {
        if (model == null || model.getId() == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        AgricultureSupply existing = supplyMapper.selectById(model.getId());
        if (existing == null || existing.getDeleted() == 1) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        AgricultureSupply supply = TransformUtils.map(model, AgricultureSupply.class);
        supply.setDeleted(existing.getDeleted()); // 保持删除状态
        supplyMapper.updateById(supply);
    }

    @Override
    public void deleteAgricultureSupply(Long id) {
        if (id == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        AgricultureSupply supply = supplyMapper.selectById(id);
        if (supply == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        supply.setDeleted(1);
        supplyMapper.updateById(supply);
    }

    private <T> IPage<T> toModelPage(IPage<AgricultureSupply> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

