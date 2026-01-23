package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.PartyActivity;
import com.maple.vms.vo.model.PartyActivityModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 党建活动分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "党建活动分页请求对象", description = "vms-党建活动分页请求对象")
public class PartyActivityPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<PartyActivity> page;

    @ApiModelProperty(value = "请求信息")
    private PartyActivityModel query;
}

