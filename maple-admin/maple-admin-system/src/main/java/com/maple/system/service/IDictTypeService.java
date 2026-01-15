package com.maple.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.system.vo.model.DictTypeModel;
import com.maple.system.vo.query.DictTypePageQuery;

import java.util.List;

public interface IDictTypeService {

    IPage<DictTypeModel> getPageList(DictTypePageQuery query);

    DictTypeModel getDictTypeById(Long id);

    Long createDictType(DictTypeModel model);

    void updateDictType(DictTypeModel model);

    Integer deleteDictType(Long id);

    List<DictTypeModel> getDictTypeList();
    
}