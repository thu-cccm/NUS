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
 * 党员积分实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_party_score")
@ApiModel(value = "党员积分对象", description = "vms-党员积分信息表")
public class PartyScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "党员ID(关联vms_resident)")
    private Long residentId;

    @ApiModelProperty(value = "积分类型(志愿服务/学习活动/其他)")
    private String scoreType;

    @ApiModelProperty(value = "积分值")
    private Integer score;

    @ApiModelProperty(value = "积分说明")
    private String description;

    @ApiModelProperty(value = "关联活动ID")
    private Long activityId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标志(0正常,1删除)")
    private Integer deleted;
}

