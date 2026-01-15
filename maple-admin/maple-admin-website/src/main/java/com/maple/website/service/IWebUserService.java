package com.maple.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.website.vo.login.AppletAuthModel;
import com.maple.website.vo.login.LoginVo;
import com.maple.website.vo.model.WebUserModel;
import com.maple.website.vo.query.WebUserPageQuery;

public interface IWebUserService {

    IPage<WebUserModel> getPageList(WebUserPageQuery query);

    WebUserModel getWebUserById(Long id);

    Long createWebUser(WebUserModel model);

    WebUserModel updateWebUser(WebUserModel model);

    Integer deleteWebUser(Long id);

    AppletAuthModel getUnlimitedQrCode();

    LoginVo checkAppletAuth(String uniCode);

    void notifyAppletAuthResult(AppletAuthModel authModel);

    WebUserModel getUserInfo();

}
