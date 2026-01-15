package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.website.bean.WebUserOperation;
import com.maple.website.vo.model.WebUserOperationModel;
import org.apache.ibatis.annotations.Param;

public interface WebUserOperationMapper extends BaseMapper<WebUserOperation>{

    IPage<WebUserOperationModel> getPageList(@Param("page") Page<WebUserOperationModel> page, @Param("webUserOperation") WebUserOperationModel webUserOperation);
}
