package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 土地资源展示模型.
 */
@Data
@ApiModel(value = "土地资源模型", description = "vms-土地资源展示模型")
public class LandModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联户主ID")
    private Long residentId;

    @ApiModelProperty(value = "地块编号")
    private String landCode;

    @ApiModelProperty(value = "面积(亩)")
    private BigDecimal area;

    @ApiModelProperty(value = "土地类型")
    private String type;

    @ApiModelProperty(value = "地理位置描述")
    private String location;

    @ApiModelProperty(value = "确权状态(0待确权,1已确权)")
    private Integer status;

    @ApiModelProperty(value = "确权证书图片路径")
    private String certUrl;
}

