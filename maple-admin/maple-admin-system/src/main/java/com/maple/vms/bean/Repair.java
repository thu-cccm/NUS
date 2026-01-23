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
 * 设施报修实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_repair")
@ApiModel(value = "设施报修对象", description = "vms-设施报修信息表")
public class Repair implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

