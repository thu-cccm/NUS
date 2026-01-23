package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Repair;
import com.maple.vms.vo.model.RepairModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 设施报修分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "设施报修分页请求对象", description = "vms-设施报修分页请求对象")
public class RepairPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Repair> page;

    @ApiModelProperty(value = "请求信息")
    private RepairModel query;
}

