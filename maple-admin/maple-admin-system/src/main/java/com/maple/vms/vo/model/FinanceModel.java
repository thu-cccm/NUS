package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务流水展示模型.
 */
@Data
@ApiModel(value = "财务流水模型", description = "vms-财务流水展示模型")
public class FinanceModel {

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
    private Date createTime;
}

