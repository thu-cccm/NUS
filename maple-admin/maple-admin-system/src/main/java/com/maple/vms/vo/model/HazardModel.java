package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 安全隐患排查展示模型.
 */
@Data
@ApiModel(value = "安全隐患排查模型", description = "vms-安全隐患排查展示模型")
public class HazardModel {

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
    private Date createTime;
}

