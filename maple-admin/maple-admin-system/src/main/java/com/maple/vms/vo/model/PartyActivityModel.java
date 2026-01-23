package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 党建活动展示模型.
 */
@Data
@ApiModel(value = "党建活动模型", description = "vms-党建活动展示模型")
public class PartyActivityModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动类型(三会一课/主题党日/其他)")
    private String type;

    @ApiModelProperty(value = "开展时间")
    private Date holdTime;

    @ApiModelProperty(value = "活动地点")
    private String location;

    @ApiModelProperty(value = "参会人员名单")
    private String participants;

    @ApiModelProperty(value = "参会人员列表(前端使用)")
    private List<String> participantList;

    @ApiModelProperty(value = "会议纪要")
    private String summary;

    @ApiModelProperty(value = "活动照片(多图URL逗号分隔)")
    private String photos;

    @ApiModelProperty(value = "活动照片列表(前端使用)")
    private List<String> photoList;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

