package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.PartyScore;
import com.maple.vms.vo.model.PartyScoreModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 党员积分分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "党员积分分页请求对象", description = "vms-党员积分分页请求对象")
public class PartyScorePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<PartyScore> page;

    @ApiModelProperty(value = "请求信息")
    private PartyScoreModel query;
}

