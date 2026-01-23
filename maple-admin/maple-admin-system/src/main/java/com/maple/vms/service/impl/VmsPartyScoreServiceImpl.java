package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.PartyScore;
import com.maple.vms.bean.Resident;
import com.maple.vms.mapper.PartyScoreMapper;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.service.IVmsPartyScoreService;
import com.maple.vms.vo.model.PartyScoreModel;
import com.maple.vms.vo.query.PartyScorePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 党员积分服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsPartyScoreServiceImpl extends ServiceImpl<PartyScoreMapper, PartyScore> implements IVmsPartyScoreService {

    private final PartyScoreMapper partyScoreMapper;
    private final ResidentMapper residentMapper;

    @Override
    public IPage<PartyScoreModel> getPageList(PartyScorePageQuery query) {
        PartyScoreModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<PartyScore> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long residentId = model == null ? null : model.getResidentId();
        String scoreType = model == null ? null : model.getScoreType();
        IPage<PartyScore> result = partyScoreMapper.selectPage(page, Wrappers.lambdaQuery(PartyScore.class)
                .eq(Objects.nonNull(residentId), PartyScore::getResidentId, residentId)
                .eq(StringUtils.isNotBlank(scoreType), PartyScore::getScoreType, scoreType)
                .eq(PartyScore::getDeleted, 0)
                .orderByDesc(PartyScore::getCreateTime));
        IPage<PartyScoreModel> modelPage = toModelPage(result, PartyScoreModel.class);
        fillResidentNames(modelPage.getRecords());
        return modelPage;
    }

    @Override
    public List<PartyScoreModel> getList(PartyScorePageQuery query) {
        PartyScoreModel model = Objects.isNull(query) ? null : query.getQuery();
        Long residentId = model == null ? null : model.getResidentId();
        String scoreType = model == null ? null : model.getScoreType();
        List<PartyScore> list = partyScoreMapper.selectList(Wrappers.lambdaQuery(PartyScore.class)
                .eq(Objects.nonNull(residentId), PartyScore::getResidentId, residentId)
                .eq(StringUtils.isNotBlank(scoreType), PartyScore::getScoreType, scoreType)
                .eq(PartyScore::getDeleted, 0)
                .orderByDesc(PartyScore::getCreateTime));
        List<PartyScoreModel> models = TransformUtils.mapList(list, PartyScoreModel.class);
        fillResidentNames(models);
        return models;
    }

    @Override
    public PartyScoreModel getPartyScoreById(Long id) {
        PartyScore score = partyScoreMapper.selectById(id);
        PartyScoreModel model = TransformUtils.map(score, PartyScoreModel.class);
        if (model != null && model.getResidentId() != null) {
            Resident resident = residentMapper.selectById(model.getResidentId());
            if (resident != null) {
                model.setResidentName(resident.getRealName());
            }
        }
        return model;
    }

    @Override
    public Long createPartyScore(PartyScoreModel model) {
        PartyScore score = TransformUtils.map(model, PartyScore.class);
        if (score.getDeleted() == null) {
            score.setDeleted(0);
        }
        if (score.getScore() == null) {
            score.setScore(0);
        }
        partyScoreMapper.insert(score);
        return score.getId();
    }

    @Override
    public void updatePartyScore(PartyScoreModel model) {
        partyScoreMapper.updateById(TransformUtils.map(model, PartyScore.class));
    }

    @Override
    public void deletePartyScore(Long id) {
        PartyScore score = partyScoreMapper.selectById(id);
        if (score != null) {
            score.setDeleted(1);
            partyScoreMapper.updateById(score);
        }
    }

    @Override
    public List<Map<String, Object>> getScoreRanking(Integer limit) {
        List<PartyScore> allScores = partyScoreMapper.selectList(Wrappers.lambdaQuery(PartyScore.class)
                .eq(PartyScore::getDeleted, 0));
        
        // 按residentId分组求和
        Map<Long, Integer> scoreMap = allScores.stream()
                .collect(Collectors.groupingBy(
                        PartyScore::getResidentId,
                        Collectors.summingInt(PartyScore::getScore)
                ));
        
        // 获取resident信息并排序
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
            Resident resident = residentMapper.selectById(entry.getKey());
            if (resident != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("residentId", entry.getKey());
                item.put("residentName", resident.getRealName());
                item.put("totalScore", entry.getValue());
                ranking.add(item);
            }
        }
        
        // 按积分降序排序
        ranking.sort((a, b) -> Integer.compare((Integer) b.get("totalScore"), (Integer) a.get("totalScore")));
        
        // 限制数量
        if (limit != null && limit > 0) {
            return ranking.subList(0, Math.min(limit, ranking.size()));
        }
        return ranking;
    }

    /**
     * 填充党员姓名.
     */
    private void fillResidentNames(List<PartyScoreModel> models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        List<Long> residentIds = models.stream()
                .map(PartyScoreModel::getResidentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (residentIds.isEmpty()) {
            return;
        }
        Map<Long, String> nameMap = residentMapper.selectBatchIds(residentIds).stream()
                .collect(Collectors.toMap(Resident::getId, Resident::getRealName, (a, b) -> a));
        models.forEach(model -> model.setResidentName(nameMap.getOrDefault(model.getResidentId(), "")));
    }

    private <T> IPage<T> toModelPage(IPage<PartyScore> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

