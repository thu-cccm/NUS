package com.maple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maple.system.bean.Dept;
import com.maple.system.vo.model.DeptModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept>{
    
    List<DeptModel> getTreeList(@Param("dept") DeptModel dept);
}
