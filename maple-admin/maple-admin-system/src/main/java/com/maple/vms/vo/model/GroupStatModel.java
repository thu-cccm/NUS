package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 村组人口统计模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "村组人口统计模型", description = "vms-村组人口统计模型")
public class GroupStatModel {

    @ApiModelProperty(value = "村组编号")
    private Integer groupNo;

    @ApiModelProperty(value = "人数")
    private Long count;
}

