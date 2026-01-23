package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Finance;
import com.maple.vms.vo.model.FinanceModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 财务流水分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "财务流水分页请求对象", description = "vms-财务流水分页请求对象")
public class FinancePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Finance> page;

    @ApiModelProperty(value = "请求信息")
    private FinanceModel query;
}

