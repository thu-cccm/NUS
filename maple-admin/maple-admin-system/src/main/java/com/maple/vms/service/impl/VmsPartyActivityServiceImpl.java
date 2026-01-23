package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.PartyActivity;
import com.maple.vms.mapper.PartyActivityMapper;
import com.maple.vms.service.IVmsPartyActivityService;
import com.maple.vms.vo.model.PartyActivityModel;
import com.maple.vms.vo.query.PartyActivityPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 党建活动服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsPartyActivityServiceImpl extends ServiceImpl<PartyActivityMapper, PartyActivity> implements IVmsPartyActivityService {

    private final PartyActivityMapper partyActivityMapper;

    @Override
    public IPage<PartyActivityModel> getPageList(PartyActivityPageQuery query) {
        PartyActivityModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<PartyActivity> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String title = model == null ? null : model.getTitle();
        String type = model == null ? null : model.getType();
        IPage<PartyActivity> result = partyActivityMapper.selectPage(page, Wrappers.lambdaQuery(PartyActivity.class)
                .like(StringUtils.isNotBlank(title), PartyActivity::getTitle, title)
                .eq(StringUtils.isNotBlank(type), PartyActivity::getType, type)
                .eq(PartyActivity::getDeleted, 0)
                .orderByDesc(PartyActivity::getHoldTime)
                .orderByDesc(PartyActivity::getId));
        IPage<PartyActivityModel> modelPage = toModelPage(result, PartyActivityModel.class);
        modelPage.getRecords().forEach(this::processModel);
        return modelPage;
    }

    @Override
    public List<PartyActivityModel> getList(PartyActivityPageQuery query) {
        PartyActivityModel model = Objects.isNull(query) ? null : query.getQuery();
        String title = model == null ? null : model.getTitle();
        String type = model == null ? null : model.getType();
        List<PartyActivity> list = partyActivityMapper.selectList(Wrappers.lambdaQuery(PartyActivity.class)
                .like(StringUtils.isNotBlank(title), PartyActivity::getTitle, title)
                .eq(StringUtils.isNotBlank(type), PartyActivity::getType, type)
                .eq(PartyActivity::getDeleted, 0)
                .orderByDesc(PartyActivity::getHoldTime)
                .orderByDesc(PartyActivity::getId));
        List<PartyActivityModel> models = TransformUtils.mapList(list, PartyActivityModel.class);
        models.forEach(this::processModel);
        return models;
    }

    @Override
    public PartyActivityModel getPartyActivityById(Long id) {
        PartyActivity activity = partyActivityMapper.selectById(id);
        PartyActivityModel model = TransformUtils.map(activity, PartyActivityModel.class);
        if (model != null) {
            processModel(model);
        }
        return model;
    }

    @Override
    public Long createPartyActivity(PartyActivityModel model) {
        PartyActivity activity = TransformUtils.map(model, PartyActivity.class);
        if (activity.getDeleted() == null) {
            activity.setDeleted(0);
        }
        // 处理列表转字符串
        if (model.getParticipantList() != null && !model.getParticipantList().isEmpty()) {
            activity.setParticipants(String.join(",", model.getParticipantList()));
        }
        if (model.getPhotoList() != null && !model.getPhotoList().isEmpty()) {
            activity.setPhotos(String.join(",", model.getPhotoList()));
        }
        partyActivityMapper.insert(activity);
        return activity.getId();
    }

    @Override
    public void updatePartyActivity(PartyActivityModel model) {
        PartyActivity activity = TransformUtils.map(model, PartyActivity.class);
        // 处理列表转字符串
        if (model.getParticipantList() != null && !model.getParticipantList().isEmpty()) {
            activity.setParticipants(String.join(",", model.getParticipantList()));
        }
        if (model.getPhotoList() != null && !model.getPhotoList().isEmpty()) {
            activity.setPhotos(String.join(",", model.getPhotoList()));
        }
        partyActivityMapper.updateById(activity);
    }

    @Override
    public void deletePartyActivity(Long id) {
        PartyActivity activity = partyActivityMapper.selectById(id);
        if (activity != null) {
            activity.setDeleted(1);
            partyActivityMapper.updateById(activity);
        }
    }

    /**
     * 处理模型数据：字符串转列表.
     */
    private void processModel(PartyActivityModel model) {
        if (StringUtils.isNotBlank(model.getParticipants())) {
            model.setParticipantList(Arrays.stream(model.getParticipants().split(","))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(model.getPhotos())) {
            model.setPhotoList(Arrays.stream(model.getPhotos().split(","))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList()));
        }
    }

    private <T> IPage<T> toModelPage(IPage<PartyActivity> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

