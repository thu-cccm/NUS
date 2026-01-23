package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Service;
import com.maple.vms.mapper.ServiceMapper;
import com.maple.vms.service.IVmsServiceService;
import com.maple.vms.vo.model.ServiceModel;
import com.maple.vms.vo.query.ServicePageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 便民服务服务实现.
 */
@Component
@RequiredArgsConstructor
public class VmsServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements IVmsServiceService {

    private final ServiceMapper serviceMapper;

    @Override
    public IPage<ServiceModel> getPageList(ServicePageQuery query) {
        ServiceModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Service> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String serviceName = model == null ? null : model.getServiceName();
        String serviceType = model == null ? null : model.getServiceType();
        String category = model == null ? null : model.getCategory();
        Integer status = model == null ? null : model.getStatus();
        IPage<Service> result = serviceMapper.selectPage(page, Wrappers.lambdaQuery(Service.class)
                .like(StringUtils.isNotBlank(serviceName), Service::getServiceName, serviceName)
                .eq(StringUtils.isNotBlank(serviceType), Service::getServiceType, serviceType)
                .eq(StringUtils.isNotBlank(category), Service::getCategory, category)
                .eq(Objects.nonNull(status), Service::getStatus, status)
                .eq(Service::getDeleted, 0)
                .orderByDesc(Service::getCreateTime));
        return toModelPage(result, ServiceModel.class);
    }

    @Override
    public List<ServiceModel> getList(ServicePageQuery query) {
        ServiceModel model = Objects.isNull(query) ? null : query.getQuery();
        String serviceName = model == null ? null : model.getServiceName();
        String serviceType = model == null ? null : model.getServiceType();
        String category = model == null ? null : model.getCategory();
        Integer status = model == null ? null : model.getStatus();
        List<Service> list = serviceMapper.selectList(Wrappers.lambdaQuery(Service.class)
                .like(StringUtils.isNotBlank(serviceName), Service::getServiceName, serviceName)
                .eq(StringUtils.isNotBlank(serviceType), Service::getServiceType, serviceType)
                .eq(StringUtils.isNotBlank(category), Service::getCategory, category)
                .eq(Objects.nonNull(status), Service::getStatus, status)
                .eq(Service::getDeleted, 0)
                .orderByDesc(Service::getCreateTime));
        return TransformUtils.mapList(list, ServiceModel.class);
    }

    @Override
    public ServiceModel getServiceById(Long id) {
        return TransformUtils.map(serviceMapper.selectById(id), ServiceModel.class);
    }

    @Override
    public Long createService(ServiceModel model) {
        Service service = TransformUtils.map(model, Service.class);
        if (service.getDeleted() == null) {
            service.setDeleted(0);
        }
        if (service.getStatus() == null) {
            service.setStatus(1);
        }
        serviceMapper.insert(service);
        return service.getId();
    }

    @Override
    public void updateService(ServiceModel model) {
        serviceMapper.updateById(TransformUtils.map(model, Service.class));
    }

    @Override
    public void deleteService(Long id) {
        Service service = serviceMapper.selectById(id);
        if (service != null) {
            service.setDeleted(1);
            serviceMapper.updateById(service);
        }
    }

    private <T> IPage<T> toModelPage(IPage<Service> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

