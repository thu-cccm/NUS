package com.maple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maple.system.bean.Menu;
import com.maple.system.vo.model.MenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuModel> getTreeList(@Param("menu") MenuModel menu);

    List<MenuModel> selectMenuTreeByUserId(Long userId);

    List<String> selectMenuPermsByUserId(Long userId);
}
