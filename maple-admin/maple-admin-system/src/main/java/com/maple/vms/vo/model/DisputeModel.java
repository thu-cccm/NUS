package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 矛盾纠纷调解展示模型.
 */
@Data
@ApiModel(value = "矛盾纠纷调解模型", description = "vms-矛盾纠纷调解展示模型")
public class DisputeModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "当事人A")
    private String partyA;

    @ApiModelProperty(value = "当事人B")
    private String partyB;

    @ApiModelProperty(value = "矛盾类型(土地/邻里/家庭/其他)")
    private String conflictType;

    @ApiModelProperty(value = "发生时间")
    private Date happenTime;

    @ApiModelProperty(value = "纠纷描述")
    private String description;

    @ApiModelProperty(value = "调解员")
    private String mediator;

    @ApiModelProperty(value = "调解结果")
    private String resultContent;

    @ApiModelProperty(value = "状态(0待处理,1处理中,2已结案)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

