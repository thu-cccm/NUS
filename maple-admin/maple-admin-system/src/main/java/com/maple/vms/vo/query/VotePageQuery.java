package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Vote;
import com.maple.vms.vo.model.VoteModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 民主互动分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "民主互动分页请求对象", description = "vms-民主互动分页请求对象")
public class VotePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Vote> page;

    @ApiModelProperty(value = "请求信息")
    private VoteModel query;
}

