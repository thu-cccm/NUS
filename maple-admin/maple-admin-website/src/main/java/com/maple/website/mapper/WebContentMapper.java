package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.website.bean.WebContent;
import com.maple.website.vo.model.WebContentModel;
import org.apache.ibatis.annotations.Param;

public interface WebContentMapper extends BaseMapper<WebContent>{

    IPage<WebContentModel> getPageList(@Param("page") Page<WebContentModel> page, @Param("webContent") WebContentModel webContent);
}
