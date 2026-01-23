package com.maple.vms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.vms.bean.Project;
import com.maple.vms.vo.model.ProjectModel;
import com.maple.vms.vo.query.ProjectPageQuery;

import java.util.List;

/**
 * 工程项目服务接口.
 */
public interface IVmsProjectService extends IService<Project> {

    /**
     * 分页查询工程项目信息.
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<ProjectModel> getPageList(ProjectPageQuery query);

    /**
     * 获取工程项目列表（不分页，用于导出）.
     *
     * @param query 查询条件
     * @return 工程项目列表
     */
    List<ProjectModel> getList(ProjectPageQuery query);

    /**
     * 根据ID获取工程项目信息.
     *
     * @param id 主键
     * @return 工程项目信息
     */
    ProjectModel getProjectById(Long id);

    /**
     * 新增工程项目信息.
     *
     * @param model 工程项目模型
     * @return 主键ID
     */
    Long createProject(ProjectModel model);

    /**
     * 修改工程项目信息.
     *
     * @param model 工程项目模型
     */
    void updateProject(ProjectModel model);

    /**
     * 删除工程项目信息.
     *
     * @param id 主键
     */
    void deleteProject(Long id);
}

