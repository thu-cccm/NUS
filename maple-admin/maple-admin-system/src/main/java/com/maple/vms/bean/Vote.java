package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 民主互动投票实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_vote")
@ApiModel(value = "民主互动对象", description = "vms-民主互动投票信息表")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
}

