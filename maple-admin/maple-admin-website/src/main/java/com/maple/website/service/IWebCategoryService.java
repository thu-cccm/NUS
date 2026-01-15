package com.maple.website.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.model.WebCategoryModel;
import com.maple.website.vo.query.WebCategoryPageQuery;

public interface IWebCategoryService {

    IPage<WebCategoryModel> getPageCategory(WebCategoryPageQuery query);

    List<WebCategoryModel> getTreeList(WebCategoryModel model);

    WebCategoryModel getWebCategoryById(Long id);

    WebCategoryModel getCategoryInfo(Long id);

    Long createWebCategory(WebCategoryModel model);

    void updateWebCategory(WebCategoryModel model);

    Integer deleteWebCategory(Long id);

    List<WebCategoryModel> getCategoryList(WebCategoryModel query);
}
