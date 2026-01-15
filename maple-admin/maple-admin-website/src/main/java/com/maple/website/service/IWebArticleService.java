package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.model.WebArticleModel;
import com.maple.website.vo.home.HomeData;
import com.maple.website.vo.query.WebArticlePageQuery;

import javax.servlet.http.HttpServletResponse;

public interface IWebArticleService {

    IPage<WebArticleModel> getPageList(WebArticlePageQuery query);

    WebArticleModel getWebArticleById(Long id, Boolean isWebUser);

    Long createWebArticle(WebArticleModel model);

    void updateWebArticle(WebArticleModel model);

    Integer deleteWebArticle(Long id);

    WebArticleModel getById(Long id);

    HomeData getHomeData();

    void downResource(HttpServletResponse response, WebArticleModel articleModel);

    void initLuceneData();
}
