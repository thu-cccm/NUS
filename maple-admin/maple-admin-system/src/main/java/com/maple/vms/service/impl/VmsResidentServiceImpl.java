package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.util.JwtUtil;
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

import java.util.Objects;

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
        IPage<Resident> result = residentMapper.selectPage(page, Wrappers.lambdaQuery(Resident.class)
                .eq(Objects.nonNull(id), Resident::getId, id)
                .like(StringUtils.isNotBlank(realName), Resident::getRealName, realName)
                .eq(Objects.nonNull(groupNo), Resident::getGroupNo, groupNo)
                .eq(Objects.nonNull(isPoor), Resident::getIsPoor, isPoor)
                .eq(VmsAuthUtils.isVillager(), Resident::getId, JwtUtil.getUserId())
                .orderByDesc(Resident::getId));
        return toModelPage(result, ResidentModel.class);
    }

    @Override
    public ResidentModel getResidentById(Long id) {
        if (VmsAuthUtils.isVillager() && !Objects.equals(id, JwtUtil.getUserId())) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "无权限查看该居民信息");
        }
        return TransformUtils.map(residentMapper.selectById(id), ResidentModel.class);
    }

    @Override
    public Long createResident(ResidentModel model) {
        Resident resident = TransformUtils.map(model, Resident.class);
        residentMapper.insert(resident);
        return resident.getId();
    }

    @Override
    public void updateResident(ResidentModel model) {
        residentMapper.updateById(TransformUtils.map(model, Resident.class));
    }

    @Override
    public void deleteResident(Long id) {
        residentMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Resident> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

