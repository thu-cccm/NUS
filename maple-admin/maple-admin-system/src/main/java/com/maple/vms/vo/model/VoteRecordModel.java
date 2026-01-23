package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 投票记录展示模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "投票记录模型", description = "vms-投票记录展示模型")
public class VoteRecordModel {

    @ApiModelProperty(value = "村民ID")
    private Long residentId;

    @ApiModelProperty(value = "村民姓名")
    private String residentName;

    @ApiModelProperty(value = "是否赞成")
    private Boolean agree;

    @ApiModelProperty(value = "投票时间")
    private Date createTime;
}

