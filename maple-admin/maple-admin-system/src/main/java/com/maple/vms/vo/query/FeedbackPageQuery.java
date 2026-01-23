package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Feedback;
import com.maple.vms.vo.model.FeedbackModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 村民互动分页请求对象.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "村民互动分页请求对象", description = "vms-村民互动分页请求对象")
public class FeedbackPageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Feedback> page;

    @ApiModelProperty(value = "请求信息")
    private FeedbackModel query;
}

