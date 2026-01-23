package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 公告阅读记录模型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "公告阅读记录模型", description = "vms-公告阅读记录展示")
public class NoticeReadRecordModel {

    @ApiModelProperty(value = "村民ID")
    private Long residentId;

    @ApiModelProperty(value = "村民姓名")
    private String residentName;

    @ApiModelProperty(value = "阅读时间")
    private Date readTime;
}

