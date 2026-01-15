package com.maple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.system.bean.DictType;
import com.maple.system.vo.model.DictTypeModel;
import org.apache.ibatis.annotations.Param;

public interface DictTypeMapper extends BaseMapper<DictType>{
    
    IPage<DictTypeModel> getPageList(@Param("page") Page<DictType> page, @Param("req") DictTypeModel req);

}