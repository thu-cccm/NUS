package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知公告展示模型.
 */
@Data
@ApiModel(value = "通知公告模型", description = "vms-通知公告展示模型")
public class NoticeModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型(通知/公示/政策)")
    private String type;

    @ApiModelProperty(value = "政策附件(文件服务名)")
    private String policyFile;

    @ApiModelProperty(value = "政策分类")
    private String policyCategory;

    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    @ApiModelProperty(value = "归档状态(0未归档,1已归档)")
    private Integer archiveStatus;

    @ApiModelProperty(value = "是否已过期")
    private Boolean expired;

    @ApiModelProperty(value = "推送对象(all/party/poor)")
    private String targetGroup;

    @ApiModelProperty(value = "阅读状态(当前用户)")
    private Boolean readStatus;

    @ApiModelProperty(value = "已读人数")
    private Long readCount;

    @ApiModelProperty(value = "应读人数")
    private Long targetCount;

    @ApiModelProperty(value = "是否置顶(0否,1是)")
    private Integer isTop;

    @ApiModelProperty(value = "状态(0草稿,1发布)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

