package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 党员积分展示模型.
 */
@Data
@ApiModel(value = "党员积分模型", description = "vms-党员积分展示模型")
public class PartyScoreModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "党员ID(关联vms_resident)")
    private Long residentId;

    @ApiModelProperty(value = "党员姓名")
    private String residentName;

    @ApiModelProperty(value = "积分类型(志愿服务/学习活动/其他)")
    private String scoreType;

    @ApiModelProperty(value = "积分值")
    private Integer score;

    @ApiModelProperty(value = "积分说明")
    private String description;

    @ApiModelProperty(value = "关联活动ID")
    private Long activityId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

