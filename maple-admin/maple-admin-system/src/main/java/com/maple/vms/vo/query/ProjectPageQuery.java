package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Project;
import com.maple.vms.vo.model.ProjectModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 工程项目分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "工程项目分页请求对象", description = "vms-工程项目分页请求对象")
public class ProjectPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Project> page;

    @ApiModelProperty(value = "请求信息")
    private ProjectModel query;
}

