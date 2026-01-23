package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 投票记录分页结果.
 */
@Data
@ApiModel(value = "投票记录分页结果", description = "vms-投票记录分页结果")
public class VoteRecordPageResult {

    @ApiModelProperty(value = "当前页码")
    private long current;

    @ApiModelProperty(value = "每页条数")
    private long size;

    @ApiModelProperty(value = "总条数")
    private long total;

    @ApiModelProperty(value = "赞成数")
    private long agreeCount;

    @ApiModelProperty(value = "反对数")
    private long disagreeCount;

    @ApiModelProperty(value = "记录列表")
    private List<VoteRecordModel> records;
}

