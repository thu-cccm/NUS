package com.maple.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.generator.bean.GenTable;
import com.maple.generator.bean.GenTableColumn;

import java.util.List;

public interface IGenTableColumnService extends IService<GenTableColumn> {

    List<GenTableColumn> selectListByTableId(Long tableId);
}
