package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 办事趋势统计模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "办事趋势统计模型", description = "vms-近6个月办事趋势")
public class ApplyTrendModel {

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "申请数量")
    private Long totalCount;

    @ApiModelProperty(value = "办结数量")
    private Long finishedCount;
}

