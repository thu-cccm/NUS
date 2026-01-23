package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人口概况维度数据模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "人口概况维度", description = "人口概况维度数据")
public class PopulationOverviewModel {

    @ApiModelProperty(value = "总人口数")
    private Long totalPopulation;

    @ApiModelProperty(value = "党员人数")
    private Long partyMembers;

    @ApiModelProperty(value = "贫困户数")
    private Long poorHouseholds;

    @ApiModelProperty(value = "迁出人数")
    private Long migratedOut;
}

