package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 集体资产展示模型.
 */
@Data
@ApiModel(value = "集体资产模型", description = "vms-集体资产展示模型")
public class AssetModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产类型(农机/房产/设施/其他)")
    private String assetType;

    @ApiModelProperty(value = "原值(元)")
    private BigDecimal originalValue;

    @ApiModelProperty(value = "购买日期")
    private Date purchaseDate;

    @ApiModelProperty(value = "保管人")
    private String keeper;

    @ApiModelProperty(value = "状态(正常/维修/报废)")
    private String status;

    @ApiModelProperty(value = "存放位置")
    private String location;

    @ApiModelProperty(value = "资产描述")
    private String description;

    @ApiModelProperty(value = "租赁合同开始日期")
    private Date contractStart;

    @ApiModelProperty(value = "租赁合同结束日期")
    private Date contractEnd;

    @ApiModelProperty(value = "承租人")
    private String lessee;

    @ApiModelProperty(value = "租金(元/月)")
    private BigDecimal rentalAmount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否即将到期(合同距离到期少于30天)")
    private Boolean isExpiringSoon;
}

