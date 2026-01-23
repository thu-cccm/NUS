package com.maple.vms.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 事务申请审核请求对象.
 */
@Data
@ApiModel(value = "事务申请审核请求对象", description = "vms-事务申请审核请求对象")
public class ApplyAuditQuery {

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "状态(1通过,2驳回)")
    private Integer status;

    @ApiModelProperty(value = "回复内容")
    private String reply;

    @ApiModelProperty(value = "公示截止时间(时间戳)")
    private Long publicEndTime;
}

