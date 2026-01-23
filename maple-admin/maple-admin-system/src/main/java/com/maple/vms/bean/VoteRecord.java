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
 * 投票记录实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_vote_record")
@ApiModel(value = "投票记录对象", description = "vms-投票记录表")
public class VoteRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "投票ID")
    private Long voteId;

    @ApiModelProperty(value = "村民ID")
    private Long residentId;

    @ApiModelProperty(value = "是否赞成")
    private Boolean agree;

    @ApiModelProperty(value = "投票时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

