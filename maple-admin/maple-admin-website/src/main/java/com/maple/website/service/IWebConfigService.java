package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.model.WebConfigModel;
import com.maple.website.vo.query.WebConfigPageQuery;

import java.util.List;

public interface IWebConfigService {

    IPage<WebConfigModel> getPageList(WebConfigPageQuery query);

    List<WebConfigModel> getAllConfigList();

    WebConfigModel getWebConfigById(Long id);

    Long createWebConfig(WebConfigModel model);

    void updateWebConfig(WebConfigModel model);

    Integer deleteWebConfig(Long id);
}
