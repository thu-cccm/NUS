package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.website.bean.WebUser;
import com.maple.website.vo.model.WebUserModel;
import org.apache.ibatis.annotations.Param;

public interface WebUserMapper extends BaseMapper<WebUser>{

    IPage<WebUserModel> getPageList(@Param("page") Page<WebUserModel> page, @Param("webUser") WebUserModel webUser);
}
