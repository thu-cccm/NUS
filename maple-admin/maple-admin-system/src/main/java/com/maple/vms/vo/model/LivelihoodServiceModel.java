package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 民生服务维度数据模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "民生服务维度", description = "民生服务维度数据")
public class LivelihoodServiceModel {

    @ApiModelProperty(value = "待办申请数")
    private Long pendingApplyCount;

    @ApiModelProperty(value = "便民服务数")
    private Long serviceCount;

    @ApiModelProperty(value = "待维修设施数")
    private Long pendingRepairCount;

    @ApiModelProperty(value = "待处理矛盾数")
    private Long pendingDisputeCount;
}

