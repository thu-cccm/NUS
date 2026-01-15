package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.website.bean.WebUserComment;
import com.maple.website.vo.model.WebUserCommentModel;
import org.apache.ibatis.annotations.Param;

public interface WebUserCommentMapper extends BaseMapper<WebUserComment>{

    IPage<WebUserCommentModel> getPageList(@Param("page") Page<WebUserCommentModel> page, @Param("webUserComment") WebUserCommentModel webUserComment);
}
