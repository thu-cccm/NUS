package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 党建活动实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_party_activity")
@ApiModel(value = "党建活动对象", description = "vms-党建活动信息表")
public class PartyActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty(value = "参会人员名单(JSON或逗号分隔)")
    private String participants;

    @ApiModelProperty(value = "会议纪要")
    private String summary;

    @ApiModelProperty(value = "活动照片(多图URL逗号分隔)")
    private String photos;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标志(0正常,1删除)")
    private Integer deleted;
}

