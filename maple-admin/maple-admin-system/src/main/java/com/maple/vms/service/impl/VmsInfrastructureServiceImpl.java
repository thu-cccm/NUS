package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Infrastructure;
import com.maple.vms.mapper.InfrastructureMapper;
import com.maple.vms.service.IVmsInfrastructureService;
import com.maple.vms.vo.model.InfrastructureModel;
import com.maple.vms.vo.query.InfrastructurePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 基础设施服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsInfrastructureServiceImpl extends ServiceImpl<InfrastructureMapper, Infrastructure> implements IVmsInfrastructureService {

    private final InfrastructureMapper infrastructureMapper;

    @Override
    public IPage<InfrastructureModel> getPageList(InfrastructurePageQuery query) {
        InfrastructureModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Infrastructure> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String projectName = model == null ? null : model.getProjectName();
        String status = model == null ? null : model.getStatus();
        IPage<Infrastructure> result = infrastructureMapper.selectPage(page, Wrappers.lambdaQuery(Infrastructure.class)
                .like(StringUtils.isNotBlank(projectName), Infrastructure::getProjectName, projectName)
                .eq(StringUtils.isNotBlank(status), Infrastructure::getStatus, status)
                .orderByDesc(Infrastructure::getId));
        return toModelPage(result, InfrastructureModel.class);
    }

    @Override
    public InfrastructureModel getInfrastructureById(Long id) {
        return TransformUtils.map(infrastructureMapper.selectById(id), InfrastructureModel.class);
    }

    @Override
    public Long createInfrastructure(InfrastructureModel model) {
        Infrastructure infrastructure = TransformUtils.map(model, Infrastructure.class);
        infrastructureMapper.insert(infrastructure);
        return infrastructure.getId();
    }

    @Override
    public void updateInfrastructure(InfrastructureModel model) {
        infrastructureMapper.updateById(TransformUtils.map(model, Infrastructure.class));
    }

    @Override
    public void deleteInfrastructure(Long id) {
        infrastructureMapper.deleteById(id);
    }

    private <T> IPage<T> toModelPage(IPage<Infrastructure> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

