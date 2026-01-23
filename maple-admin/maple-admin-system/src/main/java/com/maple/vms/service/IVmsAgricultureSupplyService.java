package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.AgricultureSupply;
import com.maple.vms.vo.model.AgricultureSupplyModel;
import com.maple.vms.vo.query.AgricultureSupplyPageQuery;

import java.util.List;

/**
 * 农资发放记录服务接口.
 */
public interface IVmsAgricultureSupplyService extends IService<AgricultureSupply> {

    IPage<AgricultureSupplyModel> getPageList(AgricultureSupplyPageQuery query);

    List<AgricultureSupplyModel> getList(AgricultureSupplyPageQuery query);

    AgricultureSupplyModel getAgricultureSupplyById(Long id);

    Long createAgricultureSupply(AgricultureSupplyModel model);

    void updateAgricultureSupply(AgricultureSupplyModel model);

    void deleteAgricultureSupply(Long id);
}

