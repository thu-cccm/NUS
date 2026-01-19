package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Agriculture;
import com.maple.vms.mapper.AgricultureMapper;
import com.maple.vms.service.IVmsAgricultureService;
import com.maple.vms.vo.model.AgricultureModel;
import com.maple.vms.vo.query.AgriculturePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 农业生产服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsAgricultureServiceImpl extends ServiceImpl<AgricultureMapper, Agriculture> implements IVmsAgricultureService {

    private final AgricultureMapper agricultureMapper;

    @Override
    public IPage<AgricultureModel> getPageList(AgriculturePageQuery query) {
        AgricultureModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Agriculture> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long residentId = model == null ? null : model.getResidentId();
        String season = model == null ? null : model.getSeason();
        String cropName = model == null ? null : model.getCropName();
        IPage<Agriculture> result = agricultureMapper.selectPage(page, Wrappers.lambdaQuery(Agriculture.class)
                .eq(Objects.nonNull(residentId), Agriculture::getResidentId, residentId)
                .like(StringUtils.isNotBlank(season), Agriculture::getSeason, season)
                .like(StringUtils.isNotBlank(cropName), Agriculture::getCropName, cropName)
                .orderByDesc(Agriculture::getId));
        return toModelPage(result, AgricultureModel.class);
    }

    @Override
    public AgricultureModel getAgricultureById(Long id) {
        return TransformUtils.map(agricultureMapper.selectById(id), AgricultureModel.class);
    }

    @Override
    public Long createAgriculture(AgricultureModel model) {
        Agriculture agriculture = TransformUtils.map(model, Agriculture.class);
        agricultureMapper.insert(agriculture);
        return agriculture.getId();
    }

    @Override
    public void updateAgriculture(AgricultureModel model) {
        agricultureMapper.updateById(TransformUtils.map(model, Agriculture.class));
    }

    @Override
    public void deleteAgriculture(Long id) {
        agricultureMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Agriculture> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

