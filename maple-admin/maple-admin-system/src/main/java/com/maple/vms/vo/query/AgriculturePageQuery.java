package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Agriculture;
import com.maple.vms.vo.model.AgricultureModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 农业生产分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "农业生产分页请求对象", description = "vms-农业生产分页请求对象")
public class AgriculturePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Agriculture> page;

    @ApiModelProperty(value = "请求信息")
    private AgricultureModel query;
}

