package com.maple.vms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.common.util.TransformUtils;
import com.maple.vms.bean.Project;
import com.maple.vms.mapper.ProjectMapper;
import com.maple.vms.service.IVmsProjectService;
import com.maple.vms.vo.model.ProjectModel;
import com.maple.vms.vo.query.ProjectPageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 工程项目服务实现.
 */
@Service
@RequiredArgsConstructor
public class VmsProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IVmsProjectService {

    private final ProjectMapper projectMapper;

    @Override
    public IPage<ProjectModel> getPageList(ProjectPageQuery query) {
        ProjectModel model = Objects.isNull(query) ? null : query.getQuery();
        Page<Project> page = Objects.isNull(query) ? new Page<>() : query.getPage();
        String projectName = model == null ? null : model.getProjectName();
        String projectType = model == null ? null : model.getProjectType();
        String status = model == null ? null : model.getStatus();
        IPage<Project> result = projectMapper.selectPage(page, Wrappers.lambdaQuery(Project.class)
                .like(StringUtils.isNotBlank(projectName), Project::getProjectName, projectName)
                .eq(StringUtils.isNotBlank(projectType), Project::getProjectType, projectType)
                .eq(StringUtils.isNotBlank(status), Project::getStatus, status)
                .eq(Project::getDeleted, 0)
                .orderByDesc(Project::getCreateTime));
        return toModelPage(result, ProjectModel.class);
    }

    @Override
    public List<ProjectModel> getList(ProjectPageQuery query) {
        ProjectModel model = Objects.isNull(query) ? null : query.getQuery();
        String projectName = model == null ? null : model.getProjectName();
        String projectType = model == null ? null : model.getProjectType();
        String status = model == null ? null : model.getStatus();
        List<Project> list = projectMapper.selectList(Wrappers.lambdaQuery(Project.class)
                .like(StringUtils.isNotBlank(projectName), Project::getProjectName, projectName)
                .eq(StringUtils.isNotBlank(projectType), Project::getProjectType, projectType)
                .eq(StringUtils.isNotBlank(status), Project::getStatus, status)
                .eq(Project::getDeleted, 0)
                .orderByDesc(Project::getCreateTime));
        return TransformUtils.mapList(list, ProjectModel.class);
    }

    @Override
    public ProjectModel getProjectById(Long id) {
        return TransformUtils.map(projectMapper.selectById(id), ProjectModel.class);
    }

    @Override
    public Long createProject(ProjectModel model) {
        Project project = TransformUtils.map(model, Project.class);
        if (project.getDeleted() == null) {
            project.setDeleted(0);
        }
        if (project.getProgress() == null) {
            project.setProgress(0);
        }
        projectMapper.insert(project);
        return project.getId();
    }

    @Override
    public void updateProject(ProjectModel model) {
        projectMapper.updateById(TransformUtils.map(model, Project.class));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectMapper.selectById(id);
        if (project != null) {
            project.setDeleted(1);
            projectMapper.updateById(project);
        }
    }

    private <T> IPage<T> toModelPage(IPage<Project> page, Class<T> clazz) {
        IPage<T> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransformUtils.mapList(page.getRecords(), clazz));
        return result;
    }
}

