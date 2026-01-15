package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.query.WebContentPageQuery;
import com.maple.website.vo.model.WebContentModel;

public interface IWebContentService {

    IPage<WebContentModel> getPageList(WebContentPageQuery query);

    WebContentModel getWebContentById(Long id);

    Long createWebContent(WebContentModel model);

    void updateWebContent(WebContentModel model);

    Integer deleteWebContent(Long id);
}
