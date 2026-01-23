package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Hazard;
import com.maple.vms.mapper.HazardMapper;
import com.maple.vms.service.IVmsHazardService;
import com.maple.vms.vo.model.HazardModel;
import com.maple.vms.vo.query.HazardPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 安全隐患排查服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsHazardServiceImpl extends ServiceImpl<HazardMapper, Hazard> implements IVmsHazardService {

    private final HazardMapper hazardMapper;

    @Override
    public IPage<HazardModel> getPageList(HazardPageQuery query) {
        HazardModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Hazard> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String hazardType = model == null ? null : model.getHazardType();
        String location = model == null ? null : model.getLocation();
        Integer status = model == null ? null : model.getStatus();
        IPage<Hazard> result = hazardMapper.selectPage(page, Wrappers.lambdaQuery(Hazard.class)
                .like(StringUtils.isNotBlank(hazardType), Hazard::getHazardType, hazardType)
                .like(StringUtils.isNotBlank(location), Hazard::getLocation, location)
                .eq(Objects.nonNull(status), Hazard::getStatus, status)
                .eq(Hazard::getDeleted, 0)
                .orderByDesc(Hazard::getCreateTime));
        return toModelPage(result, HazardModel.class);
    }

    @Override
    public List<HazardModel> getList(HazardPageQuery query) {
        HazardModel model = Objects.isNull(query) ? null : query.getQuery();
        String hazardType = model == null ? null : model.getHazardType();
        String location = model == null ? null : model.getLocation();
        Integer status = model == null ? null : model.getStatus();
        List<Hazard> list = hazardMapper.selectList(Wrappers.lambdaQuery(Hazard.class)
                .like(StringUtils.isNotBlank(hazardType), Hazard::getHazardType, hazardType)
                .like(StringUtils.isNotBlank(location), Hazard::getLocation, location)
                .eq(Objects.nonNull(status), Hazard::getStatus, status)
                .eq(Hazard::getDeleted, 0)
                .orderByDesc(Hazard::getCreateTime));
        return TransformUtils.mapList(list, HazardModel.class);
    }

    @Override
    public HazardModel getHazardById(Long id) {
        return TransformUtils.map(hazardMapper.selectById(id), HazardModel.class);
    }

    @Override
    public Long createHazard(HazardModel model) {
        Hazard hazard = TransformUtils.map(model, Hazard.class);
        if (hazard.getDeleted() == null) {
            hazard.setDeleted(0);
        }
        hazardMapper.insert(hazard);
        return hazard.getId();
    }

    @Override
    public void updateHazard(HazardModel model) {
        hazardMapper.updateById(TransformUtils.map(model, Hazard.class));
    }

    @Override
    public void deleteHazard(Long id) {
        Hazard hazard = hazardMapper.selectById(id);
        if (hazard != null) {
            hazard.setDeleted(1);
            hazardMapper.updateById(hazard);
        }
    }

    private <T> IPage<T> toModelPage(IPage<Hazard> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

