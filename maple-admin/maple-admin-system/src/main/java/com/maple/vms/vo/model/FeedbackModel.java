package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 村民互动展示模型.
 */
@Data
@ApiModel(value = "村民互动模型", description = "vms-村民互动展示模型")
public class FeedbackModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "留言内容")
    private String content;

    @ApiModelProperty(value = "是否公开(0否,1是)")
    private Integer isPublic;

    @ApiModelProperty(value = "回复内容")
    private String replyContent;

    @ApiModelProperty(value = "回复时间")
    private Date replyTime;

    @ApiModelProperty(value = "举报次数")
    private Integer reportCount;

    @ApiModelProperty(value = "是否隐藏(0否,1是)")
    private Integer isHidden;

    @ApiModelProperty(value = "是否已回复")
    private Boolean hasReply;

    @ApiModelProperty(value = "留言人ID")
    private Long residentId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

