package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Asset;
import com.maple.vms.vo.model.AssetModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 集体资产分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "集体资产分页请求对象", description = "vms-集体资产分页请求对象")
public class AssetPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Asset> page;

    @ApiModelProperty(value = "请求信息")
    private AssetModel query;
}

