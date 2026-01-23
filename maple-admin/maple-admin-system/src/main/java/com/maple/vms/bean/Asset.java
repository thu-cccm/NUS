package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 集体资产实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_asset")
@ApiModel(value = "集体资产对象", description = "vms-集体资产管理信息表")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标志(0正常,1删除)")
    private Integer deleted;
}

