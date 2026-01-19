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
 * 事务申请审批实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_apply")
@ApiModel(value = "事务申请对象", description = "vms-事务申请审批信息表")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty(value = "审核人姓名")
    private String auditBy;

    @ApiModelProperty(value = "审核意见")
    private String auditReply;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;
}

