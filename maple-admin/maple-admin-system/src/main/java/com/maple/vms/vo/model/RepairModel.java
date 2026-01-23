package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 设施报修展示模型.
 */
@Data
@ApiModel(value = "设施报修模型", description = "vms-设施报修展示模型")
public class RepairModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "设施名称")
    private String facilityName;

    @ApiModelProperty(value = "设施类型(路灯/井盖/护栏/其他)")
    private String facilityType;

    @ApiModelProperty(value = "报修位置")
    private String location;

    @ApiModelProperty(value = "报修描述")
    private String description;

    @ApiModelProperty(value = "报修人ID")
    private Long reporterId;

    @ApiModelProperty(value = "报修人姓名")
    private String reporterName;

    @ApiModelProperty(value = "报修人电话")
    private String reporterPhone;

    @ApiModelProperty(value = "状态(0待派单,1维修中,2已完成)")
    private Integer status;

    @ApiModelProperty(value = "派单人员")
    private String assignee;

    @ApiModelProperty(value = "派单时间")
    private Date assignTime;

    @ApiModelProperty(value = "维修结果")
    private String repairResult;

    @ApiModelProperty(value = "完成时间")
    private Date completeTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

