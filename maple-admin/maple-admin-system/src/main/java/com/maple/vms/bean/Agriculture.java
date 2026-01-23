package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 农业生产管理实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_agriculture")
@ApiModel(value = "农业生产对象", description = "vms-农业生产管理信息表")
public class Agriculture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty(value = "预估产值(元)")
    private BigDecimal estimatedValue;
}

