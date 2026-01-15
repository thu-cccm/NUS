package com.maple.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.system.vo.query.ConfigPageQuery;
import com.maple.system.vo.model.ConfigModel;

public interface IConfigService {

    IPage<ConfigModel> getPageList(ConfigPageQuery query);

    ConfigModel getConfigById(Long id);

    Long createConfig(ConfigModel model);

    void updateConfig(ConfigModel model);

    Integer deleteConfig(Long id);
}
