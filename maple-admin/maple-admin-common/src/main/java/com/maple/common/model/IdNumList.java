package com.maple.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IdNumList {

    @ApiModelProperty("分类ID")
    private Long id;
    
    @ApiModelProperty("数量")
    private Long num;
}
