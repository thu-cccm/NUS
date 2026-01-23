package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.vo.model.AgricultureSupplyModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 农资发放记录分页查询对象.
 */
@Data
@ApiModel(value = "农资发放记录分页查询", description = "vms-农资发放记录分页查询")
public class AgricultureSupplyPageQuery {

    @ApiModelProperty(value = "分页参数")
    private Page<AgricultureSupplyModel> page;

    @ApiModelProperty(value = "查询条件")
    private AgricultureSupplyModel query;
}

