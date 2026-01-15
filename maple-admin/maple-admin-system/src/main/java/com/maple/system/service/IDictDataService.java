package com.maple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.system.bean.DictData;
import com.maple.system.vo.model.DictDataModel;

import java.util.List;

public interface IDictDataService extends IService<DictData> {

    List<DictDataModel> getDictByCode(String code);
}
