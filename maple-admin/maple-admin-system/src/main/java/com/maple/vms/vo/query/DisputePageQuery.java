package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Dispute;
import com.maple.vms.vo.model.DisputeModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 矛盾纠纷调解分页请求对象.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "矛盾纠纷调解分页请求对象", description = "vms-矛盾纠纷调解分页请求对象")
public class DisputePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Dispute> page;

    @ApiModelProperty(value = "请求信息")
    private DisputeModel query;
}

