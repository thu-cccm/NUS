package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.common.model.IdNumList;
import com.maple.website.bean.WebCategory;
import com.maple.website.vo.model.WebCategoryModel;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WebCategoryMapper extends BaseMapper<WebCategory>{

    List<WebCategoryModel> getTreeList(@Param("webCategory") WebCategoryModel webCategory);

    List<IdNumList> selectCountGroupByCategory();

    IPage<WebCategoryModel> getPageList(@Param("page") Page<WebCategoryModel> page, @Param("webCategory")WebCategoryModel query);

    List<Long> getCategoryByMenuPath(@Param("menuPath") String menuPath);
}
