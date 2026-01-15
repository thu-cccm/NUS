package com.maple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.system.bean.Config;
import com.maple.system.vo.model.ConfigModel;
import org.apache.ibatis.annotations.Param;

public interface ConfigMapper extends BaseMapper<Config>{
    
    IPage<ConfigModel> getPageList(@Param("page") Page<ConfigModel> page, @Param("config") ConfigModel config, @Param("dataScope") String dataScope);
}
