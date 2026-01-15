package com.maple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.system.bean.Menu;
import com.maple.system.vo.model.MenuModel;
import com.maple.system.vo.model.RouterModel;

import java.util.List;
import java.util.Set;

public interface IMenuService extends IService<Menu> {

    List<RouterModel> selectMenuTreeByUserId(Long userId);

    Set<String> selectMenuPermsByUserId(Long userId);

    List<MenuModel> getTreeList(MenuModel model);

    MenuModel getMenuById(Long id);

    Long createMenu(MenuModel model);

    void updateMenu(MenuModel model);

    Integer deleteMenu(Long id);

}
