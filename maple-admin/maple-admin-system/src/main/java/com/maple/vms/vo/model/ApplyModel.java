package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 事务申请展示模型.
 */
@Data
@ApiModel(value = "事务申请模型", description = "vms-事务申请展示模型")
public class ApplyModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "申请人ID")
    private Long residentId;

    @ApiModelProperty(value = "申请类型")
    private String applyType;

    @ApiModelProperty(value = "申请内容")
    private String content;

    @ApiModelProperty(value = "佐证材料链接")
    private String proofFiles;

    @ApiModelProperty(value = "状态(0待审核,1已通过,2已驳回)")
    private Integer status;

    @ApiModelProperty(value = "公示状态(0未公示,1公示中,2已公示)")
    private Integer publicStatus;

    @ApiModelProperty(value = "公示开始时间")
    private Date publicStart;

    @ApiModelProperty(value = "公示截止时间")
    private Date publicEnd;

    @ApiModelProperty(value = "归档状态(0未归档,1已归档)")
    private Integer archiveStatus;

    @ApiModelProperty(value = "归档时间")
    private Date archiveTime;

    @ApiModelProperty(value = "审核人姓名")
    private String auditBy;

    @ApiModelProperty(value = "审核意见")
    private String auditReply;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;
}

