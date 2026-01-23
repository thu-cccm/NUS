package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 土地利用维度数据模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "土地利用维度", description = "土地利用维度数据")
public class LandUseOverviewModel {

    @ApiModelProperty(value = "总耕地面积(亩)")
    private BigDecimal totalCultivatedLand;

    @ApiModelProperty(value = "总宅基地面积(亩)")
    private BigDecimal totalHomestead;

    @ApiModelProperty(value = "总林地面积(亩)")
    private BigDecimal totalForestLand;

    @ApiModelProperty(value = "已确权土地数")
    private Long confirmedLandCount;
}

