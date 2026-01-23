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
 * 财务流水实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_finance")
@ApiModel(value = "财务流水对象", description = "vms-财务流水信息表")
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "类型(收入/支出)")
    private String financeType;

    @ApiModelProperty(value = "分类(租金收入/工程支出/补贴收入/其他)")
    private String category;

    @ApiModelProperty(value = "金额(元)")
    private BigDecimal amount;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "交易日期")
    private Date transactionDate;

    @ApiModelProperty(value = "付款方/收款方")
    private String payer;

    @ApiModelProperty(value = "凭证号")
    private String voucherNo;

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

