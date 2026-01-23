package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 民主互动展示模型.
 */
@Data
@ApiModel(value = "民主互动模型", description = "vms-民主互动展示模型")
public class VoteModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "议题标题")
    private String title;

    @ApiModelProperty(value = "议题详情")
    private String content;

    @ApiModelProperty(value = "是否匿名(0否,1是)")
    private Integer isAnonymous;

    @ApiModelProperty(value = "赞成票数")
    private Integer agreeCount;

    @ApiModelProperty(value = "反对票数")
    private Integer disagreeCount;

    @ApiModelProperty(value = "截止时间")
    private Date endTime;

    @ApiModelProperty(value = "状态(0进行中,1已结束)")
    private Integer status;

    @ApiModelProperty(value = "是否已投票")
    private Boolean voted;
}

