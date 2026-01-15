package com.maple.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.generator.bean.GenTable;
import com.maple.generator.vo.model.GenTableModel;
import com.maple.generator.vo.query.GenTablePageQuery;
import com.maple.generator.vo.query.GenTableQuery;

import java.util.List;
import java.util.Map;

public interface IGenTableService extends IService<GenTable> {

    IPage<GenTableModel> getGenTableList(GenTablePageQuery query);

    List<GenTableModel> getDbTableList(GenTableQuery query);

    void importGenTable(List<String> tableNames);

    GenTableModel getGenTableInfo(Long tableId);

    void updateGenTable(GenTableModel genTable);

    void deleteGenTableByIds(List<Long> tableIds);

    Map<String, String> previewCode(Long tableId);

    byte[] downloadCode(String tableName);

    void generatorCode(String tableName);

    void syncDb(String tableName);

    byte[] downloadCode(List<String> tableNames);

    void validateEdit(GenTable genTable);

}
