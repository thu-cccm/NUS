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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农资发放记录实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_agriculture_supply")
@ApiModel(value = "农资发放对象", description = "vms-农资发放记录信息表")
public class AgricultureSupply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "领取人ID")
    private Long recipientId;

    @ApiModelProperty(value = "领取人姓名")
    private String recipientName;

    @ApiModelProperty(value = "物资类型(化肥/种子/农药/其他)")
    private String supplyType;

    @ApiModelProperty(value = "物资名称")
    private String supplyName;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "发放日期")
    private Date issueDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "删除标志(0未删除,1已删除)")
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

