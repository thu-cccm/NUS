package com.maple.system.service;

import com.maple.system.vo.model.DeptModel;

import java.util.List;

public interface IDeptService {

    List<DeptModel> getTreeList(DeptModel model);

    DeptModel getDeptById(Long id);

    Long createDept(DeptModel model);

    void updateDept(DeptModel model);

    Integer deleteDept(Long id);
}
