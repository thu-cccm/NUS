package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Infrastructure;
import com.maple.vms.vo.model.InfrastructureModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 基础设施分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "基础设施分页请求对象", description = "vms-基础设施分页请求对象")
public class InfrastructurePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Infrastructure> page;

    @ApiModelProperty(value = "请求信息")
    private InfrastructureModel query;
}

