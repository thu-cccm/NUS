package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 土地面积统计模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "土地面积统计模型", description = "vms-土地面积统计")
public class LandAreaStatModel {

    @ApiModelProperty(value = "土地类型")
    private String type;

    @ApiModelProperty(value = "面积(亩)")
    private BigDecimal area;
}

