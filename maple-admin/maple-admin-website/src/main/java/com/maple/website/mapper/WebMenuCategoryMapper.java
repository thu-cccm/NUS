package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.website.bean.WebMenuCategory;
import com.maple.website.vo.model.WebMenuCategoryModel;
import org.apache.ibatis.annotations.Param;

public interface WebMenuCategoryMapper extends BaseMapper<WebMenuCategory>{

    IPage<WebMenuCategoryModel> getPageList(@Param("page") Page<WebMenuCategoryModel> page, @Param("webMenuCategory") WebMenuCategoryModel webMenuCategory);
}
