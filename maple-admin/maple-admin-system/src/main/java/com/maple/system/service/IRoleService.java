package com.maple.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.system.vo.model.RoleModel;
import com.maple.system.vo.query.RolePageQuery;

import java.util.List;

public interface IRoleService {

    IPage<RoleModel> getPageList(RolePageQuery query);

    List<RoleModel> getRoleList();

    RoleModel getRoleById(Long id);

    Long createRole(RoleModel model);

    void updateRole(RoleModel model);

    Integer deleteRole(Long id);
}
