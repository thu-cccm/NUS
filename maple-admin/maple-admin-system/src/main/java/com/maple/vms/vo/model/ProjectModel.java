package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工程项目展示模型.
 */
@Data
@ApiModel(value = "工程项目模型", description = "vms-工程项目展示模型")
public class ProjectModel {

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
    private Date createTime;
}

