package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Apply;
import com.maple.vms.vo.model.ApplyModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 事务申请分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "事务申请分页请求对象", description = "vms-事务申请分页请求对象")
public class ApplyPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Apply> page;

    @ApiModelProperty(value = "请求信息")
    private ApplyModel query;
}

