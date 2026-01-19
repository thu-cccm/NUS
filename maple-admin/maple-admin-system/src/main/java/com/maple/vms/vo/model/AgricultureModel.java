package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 农业生产展示模型.
 */
@Data
@ApiModel(value = "农业生产模型", description = "vms-农业生产展示模型")
public class AgricultureModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "种植负责人ID")
    private Long residentId;

    @ApiModelProperty(value = "种植季度")
    private String season;

    @ApiModelProperty(value = "作物名称")
    private String cropName;

    @ApiModelProperty(value = "种植面积")
    private BigDecimal plantArea;

    @ApiModelProperty(value = "预计产量(公斤)")
    private BigDecimal expectYield;

    @ApiModelProperty(value = "预估单价")
    private BigDecimal marketPrice;
}

