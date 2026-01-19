package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Resident;
import com.maple.vms.vo.model.ResidentModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 农村人口档案分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "农村人口档案分页请求对象", description = "vms-农村人口档案分页请求对象")
public class ResidentPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Resident> page;

    @ApiModelProperty(value = "请求信息")
    private ResidentModel query;
}

