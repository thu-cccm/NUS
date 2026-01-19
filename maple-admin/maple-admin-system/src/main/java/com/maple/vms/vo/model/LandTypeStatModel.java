package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 土地类型统计模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "土地类型统计模型", description = "vms-土地类型统计模型")
public class LandTypeStatModel {

    @ApiModelProperty(value = "土地类型")
    private String type;

    @ApiModelProperty(value = "数量")
    private Long count;
}

