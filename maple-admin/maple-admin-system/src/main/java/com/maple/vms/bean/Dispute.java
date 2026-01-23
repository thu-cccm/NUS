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
 * 矛盾纠纷调解实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_dispute")
@ApiModel(value = "矛盾纠纷调解对象", description = "vms-矛盾纠纷调解信息表")
public class Dispute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

