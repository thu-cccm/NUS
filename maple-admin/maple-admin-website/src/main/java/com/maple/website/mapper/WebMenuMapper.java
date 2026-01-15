package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maple.website.bean.WebMenu;
import com.maple.website.vo.model.WebMenuModel;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WebMenuMapper extends BaseMapper<WebMenu>{

    List<WebMenuModel> getTreeList(@Param("webMenu") WebMenuModel webMenu);
}
