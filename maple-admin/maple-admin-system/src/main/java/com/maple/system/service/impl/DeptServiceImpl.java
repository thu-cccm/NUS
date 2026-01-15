package com.maple.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.system.bean.Dept;
import com.maple.system.mapper.DeptMapper;
import com.maple.system.service.IDeptService;
import com.maple.system.vo.model.DeptModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final DeptMapper deptMapper;

    @Override
    public List<DeptModel> getTreeList(DeptModel dept) {
        List<DeptModel> list = deptMapper.getTreeList(dept);
        return getChildPerms(list, 0L);
    }

    @Override
    public DeptModel getDeptById(Long id) {
        return TransformUtils.map(deptMapper.selectById(id), DeptModel.class);
    }

    @Override
    public Long createDept(DeptModel model) {
        Dept dept = TransformUtils.map(model, Dept.class);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    public void updateDept(DeptModel model) {
        deptMapper.updateById(TransformUtils.map(model, Dept.class));
    }

    @Override
    public Integer deleteDept(Long id) {
        return deptMapper.deleteById(id);
    }

    public List<DeptModel> getChildPerms(List<DeptModel> list, Long parentId) {
        List<DeptModel> returnList = new ArrayList<>();
        for (DeptModel model : list) {
            
            if (model.getParentId().equals(parentId)) {
                recursionFn(list, model);
                returnList.add(model);
            }
        }
        return returnList;
    }

    private void recursionFn(List<DeptModel> list, DeptModel model) {
        
        List<DeptModel> childList = getChildList(list, model);
        model.setChildren(childList);
        for (DeptModel child : childList) {
            
            if (!CollectionUtils.isEmpty(getChildList(list, model))) {
                recursionFn(list, child);
            }
        }
    }

    private List<DeptModel> getChildList(List<DeptModel> list, DeptModel model) {
        List<DeptModel> modelList = new ArrayList<>();
        for (DeptModel n : list) {
            if (n.getParentId().equals(model.getId())) {
                modelList.add(n);
            }
        }
        return modelList;
    }
}
