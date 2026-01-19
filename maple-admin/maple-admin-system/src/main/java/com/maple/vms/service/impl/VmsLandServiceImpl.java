package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Land;
import com.maple.vms.mapper.LandMapper;
import com.maple.vms.service.IVmsLandService;
import com.maple.vms.vo.model.LandModel;
import com.maple.vms.vo.query.LandPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 土地资源服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsLandServiceImpl extends ServiceImpl<LandMapper, Land> implements IVmsLandService {

    private final LandMapper landMapper;

    @Override
    public IPage<LandModel> getPageList(LandPageQuery query) {
        LandModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Land> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long residentId = model == null ? null : model.getResidentId();
        String landCode = model == null ? null : model.getLandCode();
        String type = model == null ? null : model.getType();
        Integer status = model == null ? null : model.getStatus();
        IPage<Land> result = landMapper.selectPage(page, Wrappers.lambdaQuery(Land.class)
                .eq(Objects.nonNull(residentId), Land::getResidentId, residentId)
                .like(StringUtils.isNotBlank(landCode), Land::getLandCode, landCode)
                .eq(StringUtils.isNotBlank(type), Land::getType, type)
                .eq(Objects.nonNull(status), Land::getStatus, status)
                .orderByDesc(Land::getId));
        return toModelPage(result, LandModel.class);
    }

    @Override
    public LandModel getLandById(Long id) {
        return TransformUtils.map(landMapper.selectById(id), LandModel.class);
    }

    @Override
    public Long createLand(LandModel model) {
        Land land = TransformUtils.map(model, Land.class);
        landMapper.insert(land);
        return land.getId();
    }

    @Override
    public void updateLand(LandModel model) {
        landMapper.updateById(TransformUtils.map(model, Land.class));
    }

    @Override
    public void deleteLand(Long id) {
        landMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Land> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

