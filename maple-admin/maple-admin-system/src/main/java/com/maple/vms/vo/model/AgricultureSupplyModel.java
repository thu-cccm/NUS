package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 农资发放记录展示模型.
 */
@Data
@ApiModel(value = "农资发放记录模型", description = "vms-农资发放记录展示模型")
public class AgricultureSupplyModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "领取人ID")
    private Long recipientId;

    @ApiModelProperty(value = "领取人姓名")
    private String recipientName;

    @ApiModelProperty(value = "物资类型")
    private String supplyType;

    @ApiModelProperty(value = "物资名称")
    private String supplyName;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "发放日期")
    private Date issueDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

