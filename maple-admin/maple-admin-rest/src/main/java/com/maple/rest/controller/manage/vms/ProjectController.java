package com.maple.rest.controller.manage.vms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.vms.service.IVmsProjectService;
import com.maple.vms.vo.model.ProjectModel;
import com.maple.vms.vo.query.ProjectPageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 工程项目接口.
 */
@Api(tags = "新农村管理-工程项目接口")
@RestController
@RequestMapping("/manage/vms/project")
@RequiredArgsConstructor
public class ProjectController {

    private final IVmsProjectService projectService;

    @ApiOperation(value = "获取工程项目列表")
    @PostMapping("/getPageList")
    public IPage<ProjectModel> getPageList(@RequestBody ProjectPageQuery req) {
        return projectService.getPageList(req);
    }

    @ApiOperation(value = "根据ID查询工程项目信息")
    @GetMapping("/{id}")
    public ProjectModel getProjectById(@PathVariable("id") Long id) {
        return projectService.getProjectById(id);
    }

    @ApiOperation(value = "新增工程项目信息")
    @PostMapping("/create")
    public Long createProject(@RequestBody ProjectModel model) {
        return projectService.createProject(model);
    }

    @ApiOperation(value = "修改工程项目信息")
    @PostMapping("/update")
    public void updateProject(@RequestBody ProjectModel model) {
        projectService.updateProject(model);
    }

    @ApiOperation(value = "删除工程项目信息")
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
    }

    @ApiOperation(value = "导出工程项目信息")
    @GetMapping("/export")
    public void exportProject(@RequestParam(value = "projectName", required = false) String projectName,
                             @RequestParam(value = "projectType", required = false) String projectType,
                             @RequestParam(value = "status", required = false) String status,
                             HttpServletResponse response) {
        ProjectModel model = new ProjectModel();
        model.setProjectName(projectName);
        model.setProjectType(projectType);
        model.setStatus(status);
        ProjectPageQuery query = new ProjectPageQuery();
        query.setQuery(model);
        List<ProjectModel> list = projectService.getList(query);

        String fileName = "project-export.csv";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append('\uFEFF');
            sb.append("项目名称,项目类型,预算(元),施工方,开工日期,预计完工日期,进度(%),状态,项目位置\n");
            for (ProjectModel item : list) {
                sb.append(safeCsv(item.getProjectName()))
                        .append(',')
                        .append(safeCsv(item.getProjectType()))
                        .append(',')
                        .append(item.getBudget() == null ? "" : item.getBudget())
                        .append(',')
                        .append(safeCsv(item.getContractor()))
                        .append(',')
                        .append(item.getStartDate() == null ? "" : formatter.format(item.getStartDate()))
                        .append(',')
                        .append(item.getEndDate() == null ? "" : formatter.format(item.getEndDate()))
                        .append(',')
                        .append(item.getProgress() == null ? "0" : item.getProgress())
                        .append(',')
                        .append(safeCsv(item.getStatus()))
                        .append(',')
                        .append(safeCsv(item.getLocation()))
                        .append('\n');
            }
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String safeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

