package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 村民互动实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_feedback")
@ApiModel(value = "村民互动对象", description = "vms-村民互动留言表")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty(value = "留言人ID")
    private Long residentId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

