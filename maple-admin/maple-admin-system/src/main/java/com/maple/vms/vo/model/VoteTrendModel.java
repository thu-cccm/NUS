package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投票趋势模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "投票趋势模型", description = "vms-投票趋势统计")
public class VoteTrendModel {

    @ApiModelProperty(value = "日期")
    private String day;

    @ApiModelProperty(value = "赞成票数")
    private Long agreeCount;

    @ApiModelProperty(value = "反对票数")
    private Long disagreeCount;
}

