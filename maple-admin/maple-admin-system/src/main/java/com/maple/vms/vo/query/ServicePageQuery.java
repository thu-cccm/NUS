package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Service;
import com.maple.vms.vo.model.ServiceModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 便民服务分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "便民服务分页请求对象", description = "vms-便民服务分页请求对象")
public class ServicePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Service> page;

    @ApiModelProperty(value = "请求信息")
    private ServiceModel query;
}

