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
 * 公告阅读记录实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_notice_read")
@ApiModel(value = "公告阅读记录对象", description = "vms-公告阅读记录表")
public class NoticeRead implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "公告ID")
    private Long noticeId;

    @ApiModelProperty(value = "村民ID")
    private Long residentId;

    @ApiModelProperty(value = "阅读时间")
    @TableField(fill = FieldFill.INSERT)
    private Date readTime;
}

