package com.maple.generator.mapper;

import com.maple.generator.bean.GenTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maple.generator.vo.model.GenTableColumnModel;

import java.util.List;

public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    List<GenTableColumnModel> selectDbTableColumnsByName(String tableName);
}
