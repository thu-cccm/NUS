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
 * 便民服务实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_service")
@ApiModel(value = "便民服务对象", description = "vms-便民服务信息表")
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "服务类型(医疗卫生/文化教育/就业招聘)")
    private String serviceType;

    @ApiModelProperty(value = "分类(村卫生室/小学/工厂等)")
    private String category;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "服务描述")
    private String description;

    @ApiModelProperty(value = "排班/开放时间/招工信息(JSON或文本)")
    private String schedule;

    @ApiModelProperty(value = "状态(0停用,1启用)")
    private Integer status;

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

