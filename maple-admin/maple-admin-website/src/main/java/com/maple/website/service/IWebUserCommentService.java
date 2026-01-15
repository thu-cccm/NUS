package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.query.WebUserCommentPageQuery;
import com.maple.website.vo.model.WebUserCommentModel;

public interface IWebUserCommentService {

    IPage<WebUserCommentModel> getPageList(WebUserCommentPageQuery query);

    WebUserCommentModel getWebUserCommentById(Long id);

    WebUserCommentModel createWebUserComment(WebUserCommentModel model);

    Integer deleteWebUserComment(Long id);
}
