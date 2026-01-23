package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Hazard;
import com.maple.vms.vo.model.HazardModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 安全隐患排查分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "安全隐患排查分页请求对象", description = "vms-安全隐患排查分页请求对象")
public class HazardPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Hazard> page;

    @ApiModelProperty(value = "请求信息")
    private HazardModel query;
}

