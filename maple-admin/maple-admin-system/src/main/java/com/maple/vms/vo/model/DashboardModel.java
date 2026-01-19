package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页数据看板模型.
 */
@Data
@ApiModel(value = "数据看板模型", description = "vms-首页数据看板展示模型")
public class DashboardModel {

    @ApiModelProperty(value = "总人口数")
    private Long totalResidents;

    @ApiModelProperty(value = "党员人数")
    private Long partyMembers;

    @ApiModelProperty(value = "贫困户数")
    private Long poorHouseholds;

    @ApiModelProperty(value = "本月待办申请数")
    private Long pendingApplyCount;

    @ApiModelProperty(value = "土地类型分布")
    private List<LandTypeStatModel> landTypeStats;

    @ApiModelProperty(value = "村组人口分布")
    private List<GroupStatModel> groupStats;
}

