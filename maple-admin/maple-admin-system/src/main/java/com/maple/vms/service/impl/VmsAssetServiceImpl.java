package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Asset;
import com.maple.vms.mapper.AssetMapper;
import com.maple.vms.service.IVmsAssetService;
import com.maple.vms.vo.model.AssetModel;
import com.maple.vms.vo.query.AssetPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 集体资产服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsAssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IVmsAssetService {

    private final AssetMapper assetMapper;

    @Override
    public IPage<AssetModel> getPageList(AssetPageQuery query) {
        AssetModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Asset> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String assetName = model == null ? null : model.getAssetName();
        String assetType = model == null ? null : model.getAssetType();
        String status = model == null ? null : model.getStatus();
        String keeper = model == null ? null : model.getKeeper();
        IPage<Asset> result = assetMapper.selectPage(page, Wrappers.lambdaQuery(Asset.class)
                .like(StringUtils.isNotBlank(assetName), Asset::getAssetName, assetName)
                .eq(StringUtils.isNotBlank(assetType), Asset::getAssetType, assetType)
                .eq(StringUtils.isNotBlank(status), Asset::getStatus, status)
                .like(StringUtils.isNotBlank(keeper), Asset::getKeeper, keeper)
                .eq(Asset::getDeleted, 0)
                .orderByDesc(Asset::getId));
        IPage<AssetModel> modelPage = toModelPage(result, AssetModel.class);
        // 计算是否即将到期
        modelPage.getRecords().forEach(this::calculateExpiringSoon);
        return modelPage;
    }

    @Override
    public List<AssetModel> getList(AssetPageQuery query) {
        AssetModel model = Objects.isNull(query) ? null : query.getQuery();
        String assetName = model == null ? null : model.getAssetName();
        String assetType = model == null ? null : model.getAssetType();
        String status = model == null ? null : model.getStatus();
        String keeper = model == null ? null : model.getKeeper();
        List<Asset> list = assetMapper.selectList(Wrappers.lambdaQuery(Asset.class)
                .like(StringUtils.isNotBlank(assetName), Asset::getAssetName, assetName)
                .eq(StringUtils.isNotBlank(assetType), Asset::getAssetType, assetType)
                .eq(StringUtils.isNotBlank(status), Asset::getStatus, status)
                .like(StringUtils.isNotBlank(keeper), Asset::getKeeper, keeper)
                .eq(Asset::getDeleted, 0)
                .orderByDesc(Asset::getId));
        List<AssetModel> models = TransformUtils.mapList(list, AssetModel.class);
        models.forEach(this::calculateExpiringSoon);
        return models;
    }

    @Override
    public AssetModel getAssetById(Long id) {
        Asset asset = assetMapper.selectById(id);
        AssetModel model = TransformUtils.map(asset, AssetModel.class);
        if (model != null) {
            calculateExpiringSoon(model);
        }
        return model;
    }

    @Override
    public Long createAsset(AssetModel model) {
        Asset asset = TransformUtils.map(model, Asset.class);
        if (asset.getDeleted() == null) {
            asset.setDeleted(0);
        }
        assetMapper.insert(asset);
        return asset.getId();
    }

    @Override
    public void updateAsset(AssetModel model) {
        assetMapper.updateById(TransformUtils.map(model, Asset.class));
    }

    @Override
    public void deleteAsset(Long id) {
        Asset asset = assetMapper.selectById(id);
        if (asset != null) {
            asset.setDeleted(1);
            assetMapper.updateById(asset);
        }
    }

    /**
     * 计算是否即将到期（合同距离到期少于30天）.
     */
    private void calculateExpiringSoon(AssetModel model) {
        if (model.getContractEnd() == null) {
            model.setIsExpiringSoon(false);
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date thirtyDaysLater = cal.getTime();
        model.setIsExpiringSoon(model.getContractEnd().before(thirtyDaysLater) && model.getContractEnd().after(new Date()));
    }

    private <T> IPage<T> toModelPage(IPage<Asset> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

