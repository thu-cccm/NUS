package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础设施展示模型.
 */
@Data
@ApiModel(value = "基础设施模型", description = "vms-基础设施展示模型")
public class InfrastructureModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目预算")
    private BigDecimal budget;

    @ApiModelProperty(value = "开工日期")
    private Date startDate;

    @ApiModelProperty(value = "施工单位")
    private String contractor;

    @ApiModelProperty(value = "工程进度(0-100)")
    private Integer progress;

    @ApiModelProperty(value = "状态")
    private String status;
}

