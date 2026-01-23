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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
        IPage<AgricultureModel> modelPage = toModelPage(result, AgricultureModel.class);
        // 计算产值预估
        modelPage.getRecords().forEach(this::calculateEstimatedValue);
        return modelPage;
    }

    @Override
    public List<AgricultureModel> getList(AgriculturePageQuery query) {
        AgricultureModel model = Objects.isNull(query) ? null : query.getQuery();
        Long residentId = model == null ? null : model.getResidentId();
        String season = model == null ? null : model.getSeason();
        String cropName = model == null ? null : model.getCropName();
        List<Agriculture> list = agricultureMapper.selectList(Wrappers.lambdaQuery(Agriculture.class)
                .eq(Objects.nonNull(residentId), Agriculture::getResidentId, residentId)
                .like(StringUtils.isNotBlank(season), Agriculture::getSeason, season)
                .like(StringUtils.isNotBlank(cropName), Agriculture::getCropName, cropName)
                .orderByDesc(Agriculture::getId));
        List<AgricultureModel> models = TransformUtils.mapList(list, AgricultureModel.class);
        models.forEach(this::calculateEstimatedValue);
        return models;
    }

    @Override
    public AgricultureModel getAgricultureById(Long id) {
        if (id == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        Agriculture agriculture = agricultureMapper.selectById(id);
        if (agriculture == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        AgricultureModel model = TransformUtils.map(agriculture, AgricultureModel.class);
        calculateEstimatedValue(model);
        return model;
    }

    @Override
    public Long createAgriculture(AgricultureModel model) {
        if (model == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        if (StringUtils.isBlank(model.getCropName())) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "作物名称不能为空");
        }
        if (model.getPlantArea() == null || model.getPlantArea().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "种植面积必须大于0");
        }
        if (model.getMarketPrice() == null || model.getMarketPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "市场单价必须大于0");
        }
        // 计算产值预估
        calculateEstimatedValue(model);
        Agriculture agriculture = TransformUtils.map(model, Agriculture.class);
        agricultureMapper.insert(agriculture);
        return agriculture.getId();
    }

    @Override
    public void updateAgriculture(AgricultureModel model) {
        if (model == null || model.getId() == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        Agriculture existing = agricultureMapper.selectById(model.getId());
        if (existing == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        if (StringUtils.isBlank(model.getCropName())) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "作物名称不能为空");
        }
        if (model.getPlantArea() != null && model.getPlantArea().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "种植面积必须大于0");
        }
        if (model.getMarketPrice() != null && model.getMarketPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "市场单价必须大于0");
        }
        // 计算产值预估
        calculateEstimatedValue(model);
        Agriculture agriculture = TransformUtils.map(model, Agriculture.class);
        agricultureMapper.updateById(agriculture);
    }

    /**
     * 计算产值预估：种植面积 × 预设亩产 × 市场单价
     * 如果已有预计产量，则使用：预计产量 × 市场单价
     */
    private void calculateEstimatedValue(AgricultureModel model) {
        if (model.getPlantArea() == null || model.getMarketPrice() == null) {
            model.setEstimatedValue(BigDecimal.ZERO);
            return;
        }
        BigDecimal estimatedValue;
        if (model.getExpectYield() != null && model.getExpectYield().compareTo(BigDecimal.ZERO) > 0) {
            // 使用预计产量计算
            estimatedValue = model.getExpectYield().multiply(model.getMarketPrice());
        } else {
            // 使用预设亩产计算（默认亩产：水稻500公斤/亩，玉米400公斤/亩，其他300公斤/亩）
            BigDecimal yieldPerMu = getDefaultYieldPerMu(model.getCropName());
            estimatedValue = model.getPlantArea().multiply(yieldPerMu).multiply(model.getMarketPrice());
        }
        model.setEstimatedValue(estimatedValue.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * 获取默认亩产（公斤/亩）
     */
    private BigDecimal getDefaultYieldPerMu(String cropName) {
        if (cropName == null) {
            return new BigDecimal("300");
        }
        if (cropName.contains("水稻")) {
            return new BigDecimal("500");
        } else if (cropName.contains("玉米")) {
            return new BigDecimal("400");
        } else {
            return new BigDecimal("300");
        }
    }

    @Override
    public void deleteAgriculture(Long id) {
        if (id == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        Agriculture agriculture = agricultureMapper.selectById(id);
        if (agriculture == null) {
            throw new com.maple.common.config.exception.MapleCheckException(com.maple.common.config.exception.ErrorCode.NOT_FIND_DATA);
        }
        agricultureMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Agriculture> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

