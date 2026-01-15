package com.maple.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.system.bean.User;
import com.maple.system.vo.model.UserModel;
import com.maple.system.vo.query.LoginQuery;
import com.maple.system.vo.query.UserPageQuery;

public interface IUserService extends IService<User> {

    UserModel login(LoginQuery req);

    IPage<UserModel> getPageList(UserPageQuery userReq);

    UserModel getUserById(Long id);

    Long createUser(UserModel model);

    void updateUser(UserModel model);

    void deleteUser(Long id);

    void logout();
}
