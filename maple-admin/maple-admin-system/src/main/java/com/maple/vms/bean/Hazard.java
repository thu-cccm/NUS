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
 * 安全隐患排查实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_hazard")
@ApiModel(value = "安全隐患排查对象", description = "vms-安全隐患排查信息表")
public class Hazard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "隐患类型(危房/池塘无护栏/其他)")
    private String hazardType;

    @ApiModelProperty(value = "隐患位置")
    private String location;

    @ApiModelProperty(value = "隐患描述")
    private String description;

    @ApiModelProperty(value = "上报人")
    private String reporter;

    @ApiModelProperty(value = "状态(0待整改,1整改中,2已整改)")
    private Integer status;

    @ApiModelProperty(value = "整改时间")
    private Date rectifyTime;

    @ApiModelProperty(value = "整改结果")
    private String rectifyResult;

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

