package com.maple.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.common.lucene.LuceneDataModel;
import com.maple.website.bean.WebArticle;
import com.maple.website.vo.model.WebArticleModel;
import com.maple.website.vo.model.WebCategoryModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface WebArticleMapper extends BaseMapper<WebArticle>{

    IPage<WebArticleModel> getPageList(@Param("page") Page<WebArticleModel> page, @Param("req") WebArticleModel req);

    WebCategoryModel selectReadAndCollectNum(@Param("categoryId") Long categoryId);

    WebArticleModel getPreviousTitle(@Param("req") WebArticleModel req);

    WebArticleModel getNextTitle(@Param("req") WebArticleModel req);

    Long getMaxSortNum(@Param("categoryId") Long categoryId);

    Cursor<LuceneDataModel> selectArticleList();
}
