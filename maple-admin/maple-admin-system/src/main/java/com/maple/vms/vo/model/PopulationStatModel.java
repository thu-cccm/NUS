package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人口结构统计模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "人口结构统计模型", description = "vms-人口结构统计")
public class PopulationStatModel {

    @ApiModelProperty(value = "分类名称")
    private String label;

    @ApiModelProperty(value = "人数")
    private Long count;
}

