package com.maple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.system.bean.Role;
import com.maple.system.vo.model.RoleModel;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper extends BaseMapper<Role>{
    
    IPage<RoleModel> getPageList(@Param("page") Page<RoleModel> page, @Param("role") RoleModel role);
}
