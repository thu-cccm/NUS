package com.maple.vms.vo.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.vms.bean.Notice;
import com.maple.vms.vo.model.NoticeModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知公告分页请求对象.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通知公告分页请求对象", description = "vms-通知公告分页请求对象")
public class NoticePageQuery {

    @ApiModelProperty(value = "分页信息")
    private Page<Notice> page;

    @ApiModelProperty(value = "请求信息")
    private NoticeModel query;
}

