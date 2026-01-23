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
 * 通知公告实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_notice")
@ApiModel(value = "通知公告对象", description = "vms-通知公告信息表")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型(通知/公示/政策)")
    private String type;

    @ApiModelProperty(value = "政策附件(文件服务名)")
    @TableField("policy_file")
    private String policyFile;

    @ApiModelProperty(value = "政策分类")
    @TableField("policy_category")
    private String policyCategory;

    @ApiModelProperty(value = "过期时间")
    @TableField("expire_time")
    private Date expireTime;

    @ApiModelProperty(value = "归档状态(0未归档,1已归档)")
    @TableField("archive_status")
    private Integer archiveStatus;

    @ApiModelProperty(value = "推送对象(all/party/poor)")
    private String targetGroup;

    @ApiModelProperty(value = "是否置顶(0否,1是)")
    private Integer isTop;

    @ApiModelProperty(value = "状态(0草稿,1发布)")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

