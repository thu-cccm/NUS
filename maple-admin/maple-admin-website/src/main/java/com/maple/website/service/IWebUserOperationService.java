package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.bean.WebUserOperation;
import com.maple.website.common.enums.DataTypeEnum;
import com.maple.website.vo.query.WebUserOperationPageQuery;
import com.maple.website.vo.model.WebUserOperationModel;

public interface IWebUserOperationService {

    IPage<WebUserOperationModel> getPageList(WebUserOperationPageQuery query);

    WebUserOperationModel getWebUserOperationById(Long id);

    void collect(Long dataId, Boolean isCollect, Integer typeEnum);

    void like(Long dataId, Boolean isLike, Integer typeEnum);

    void read(Long dataId, Integer typeEnum);

    void download(WebUserOperationModel model, Integer typeEnum);
}
