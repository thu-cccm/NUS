package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
import com.alibaba.fastjson.JSON;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Resident;
import com.maple.vms.mapper.ResidentMapper;
import com.maple.vms.service.IVmsResidentService;
import com.maple.vms.util.VmsAuthUtils;
import com.maple.vms.vo.model.ResidentModel;
import com.maple.vms.vo.query.ResidentPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 农村人口档案服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsResidentServiceImpl extends ServiceImpl<ResidentMapper, Resident> implements IVmsResidentService {

    private final ResidentMapper residentMapper;

    @Override
    public IPage<ResidentModel> getPageList(ResidentPageQuery query) {
        ResidentModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Resident> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        Long id = model == null ? null : model.getId();
        String realName = model == null ? null : model.getRealName();
        Integer groupNo = model == null ? null : model.getGroupNo();
        Integer isPoor = model == null ? null : model.getIsPoor();
        String familyId = model == null ? null : model.getFamilyId();
        String migrateStatus = model == null ? null : model.getMigrateStatus();
        IPage<Resident> result = residentMapper.selectPage(page, Wrappers.lambdaQuery(Resident.class)
                .eq(Objects.nonNull(id), Resident::getId, id)
                .like(StringUtils.isNotBlank(realName), Resident::getRealName, realName)
                .eq(Objects.nonNull(groupNo), Resident::getGroupNo, groupNo)
                .eq(Objects.nonNull(isPoor), Resident::getIsPoor, isPoor)
                .eq(StringUtils.isNotBlank(familyId), Resident::getFamilyId, familyId)
                .eq(StringUtils.isNotBlank(migrateStatus), Resident::getMigrateStatus, migrateStatus)
                .eq(VmsAuthUtils.isVillager(), Resident::getId, JwtUtil.getUserId())
                .orderByDesc(Resident::getIsHouseholder)
                .orderByAsc(Resident::getFamilyId)
                .orderByDesc(Resident::getId));
        IPage<ResidentModel> modelPage = toModelPage(result, ResidentModel.class);
        modelPage.getRecords().forEach(this::processModel);
        return modelPage;
    }

    @Override
    public List<ResidentModel> getList(ResidentPageQuery query) {
        ResidentModel model = Objects.isNull(query) ? null : query.getQuery();
        Long id = model == null ? null : model.getId();
        String realName = model == null ? null : model.getRealName();
        Integer groupNo = model == null ? null : model.getGroupNo();
        Integer isPoor = model == null ? null : model.getIsPoor();
        String familyId = model == null ? null : model.getFamilyId();
        String migrateStatus = model == null ? null : model.getMigrateStatus();
        List<Resident> list = residentMapper.selectList(Wrappers.lambdaQuery(Resident.class)
                .eq(Objects.nonNull(id), Resident::getId, id)
                .like(StringUtils.isNotBlank(realName), Resident::getRealName, realName)
                .eq(Objects.nonNull(groupNo), Resident::getGroupNo, groupNo)
                .eq(Objects.nonNull(isPoor), Resident::getIsPoor, isPoor)
                .eq(StringUtils.isNotBlank(familyId), Resident::getFamilyId, familyId)
                .eq(StringUtils.isNotBlank(migrateStatus), Resident::getMigrateStatus, migrateStatus)
                .eq(VmsAuthUtils.isVillager(), Resident::getId, JwtUtil.getUserId())
                .orderByDesc(Resident::getIsHouseholder)
                .orderByAsc(Resident::getFamilyId)
                .orderByDesc(Resident::getId));
        List<ResidentModel> models = TransformUtils.mapList(list, ResidentModel.class);
        models.forEach(this::processModel);
        return models;
    }

    @Override
    public ResidentModel getResidentById(Long id) {
        if (VmsAuthUtils.isVillager() && !Objects.equals(id, JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该居民信息");
        }
        ResidentModel model = TransformUtils.map(residentMapper.selectById(id), ResidentModel.class);
        if (model != null) {
            processModel(model);
        }
        return model;
    }

    @Override
    public Long createResident(ResidentModel model) {
        if (model == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        if (StringUtils.isBlank(model.getRealName())) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "姓名不能为空");
        }
        if (StringUtils.isBlank(model.getIdCard())) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "身份证号不能为空");
        }
        // 检查身份证号是否已存在
        long count = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class)
                .eq(Resident::getIdCard, model.getIdCard()));
        if (count > 0) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该身份证号已存在");
        }
        Resident resident = TransformUtils.map(model, Resident.class);
        // 处理tags列表转JSON
        if (model.getTagList() != null && !model.getTagList().isEmpty()) {
            resident.setTags(JSON.toJSONString(model.getTagList()));
        }
        // 如果未设置迁移状态，默认为"在村"
        if (StringUtils.isBlank(resident.getMigrateStatus())) {
            resident.setMigrateStatus("在村");
        }
        residentMapper.insert(resident);
        return resident.getId();
    }

    @Override
    public void updateResident(ResidentModel model) {
        if (model == null || model.getId() == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "参数不能为空");
        }
        Resident existing = residentMapper.selectById(model.getId());
        if (existing == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (VmsAuthUtils.isVillager() && !Objects.equals(existing.getId(), JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限修改该居民信息");
        }
        if (StringUtils.isBlank(model.getRealName())) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "姓名不能为空");
        }
        if (StringUtils.isBlank(model.getIdCard())) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "身份证号不能为空");
        }
        // 检查身份证号是否被其他记录使用
        if (!Objects.equals(existing.getIdCard(), model.getIdCard())) {
            long count = residentMapper.selectCount(Wrappers.lambdaQuery(Resident.class)
                    .eq(Resident::getIdCard, model.getIdCard())
                    .ne(Resident::getId, model.getId()));
            if (count > 0) {
                throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该身份证号已被其他记录使用");
            }
        }
        Resident resident = TransformUtils.map(model, Resident.class);
        // 处理tags列表转JSON
        if (model.getTagList() != null && !model.getTagList().isEmpty()) {
            resident.setTags(JSON.toJSONString(model.getTagList()));
        }
        residentMapper.updateById(resident);
    }

    @Override
    public void deleteResident(Long id) {
        if (id == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        Resident resident = residentMapper.selectById(id);
        if (resident == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (VmsAuthUtils.isVillager() && !Objects.equals(id, JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限删除该居民信息");
        }
        residentMapper.deleteById(id);
    }

    @Override
    public List<ResidentModel> getResidentsByFamily(ResidentPageQuery query) {
        ResidentModel model = Objects.isNull(query) ? null : query.getQuery();
        String familyId = model == null ? null : model.getFamilyId();
        List<Resident> list = residentMapper.selectList(Wrappers.lambdaQuery(Resident.class)
                .eq(StringUtils.isNotBlank(familyId), Resident::getFamilyId, familyId)
                .eq(VmsAuthUtils.isVillager(), Resident::getId, JwtUtil.getUserId())
                .orderByDesc(Resident::getIsHouseholder)
                .orderByAsc(Resident::getFamilyId)
                .orderByDesc(Resident::getId));
        List<ResidentModel> models = TransformUtils.mapList(list, ResidentModel.class);
        models.forEach(this::processModel);
        return models;
    }

    @Override
    public void migrateOut(Long id, String migrateTo) {
        if (id == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        if (StringUtils.isBlank(migrateTo)) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "迁出地不能为空");
        }
        Resident resident = residentMapper.selectById(id);
        if (resident == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if ("迁出".equals(resident.getMigrateStatus())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该居民已迁出");
        }
        resident.setMigrateStatus("迁出");
        resident.setMigrateTo(migrateTo);
        resident.setMigrateTime(new Date());
        residentMapper.updateById(resident);
    }

    @Override
    public void migrateIn(Long id) {
        if (id == null) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR, "ID不能为空");
        }
        Resident resident = residentMapper.selectById(id);
        if (resident == null) {
            throw new MapleCheckException(ErrorCode.NOT_FIND_DATA);
        }
        if (!"迁出".equals(resident.getMigrateStatus())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "该居民未迁出，无法执行迁入操作");
        }
        resident.setMigrateStatus("迁入");
        resident.setMigrateTo(null);
        resident.setMigrateTime(new Date());
        residentMapper.updateById(resident);
    }

    /**
     * 处理模型数据：JSON转列表.
     */
    private void processModel(ResidentModel model) {
        if (StringUtils.isNotBlank(model.getTags())) {
            try {
                model.setTagList(JSON.parseArray(model.getTags(), String.class));
            } catch (Exception e) {
                // 如果JSON解析失败，尝试按逗号分隔
                model.setTagList(Arrays.stream(model.getTags().split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }
        }
    }

    private <T> IPage<T> toModelPage(IPage<Resident> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

