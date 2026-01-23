package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 便民服务展示模型.
 */
@Data
@ApiModel(value = "便民服务模型", description = "vms-便民服务展示模型")
public class ServiceModel {

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

    @ApiModelProperty(value = "排班/开放时间/招工信息")
    private String schedule;

    @ApiModelProperty(value = "状态(0停用,1启用)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

