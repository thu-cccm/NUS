package com.maple.website.service;

import java.util.List;

import com.maple.website.vo.model.WebMenuModel;

public interface IWebMenuService {

    List<WebMenuModel> getTreeList(WebMenuModel model);

    WebMenuModel getWebMenuById(Long id);

    Long createWebMenu(WebMenuModel model);

    void updateWebMenu(WebMenuModel model);

    Integer deleteWebMenu(Long id);

    WebMenuModel getWebMenuByPath(String path);
}
