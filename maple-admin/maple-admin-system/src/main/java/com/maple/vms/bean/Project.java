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
 * 工程项目实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_project")
@ApiModel(value = "工程项目对象", description = "vms-工程项目信息表")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目类型(修路/水利/路灯/其他)")
    private String projectType;

    @ApiModelProperty(value = "预算(元)")
    private BigDecimal budget;

    @ApiModelProperty(value = "施工方")
    private String contractor;

    @ApiModelProperty(value = "开工日期")
    private Date startDate;

    @ApiModelProperty(value = "预计完工日期")
    private Date endDate;

    @ApiModelProperty(value = "工程进度(0-100)")
    private Integer progress;

    @ApiModelProperty(value = "状态(规划/施工中/验收/维保)")
    private String status;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "项目位置")
    private String location;

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

