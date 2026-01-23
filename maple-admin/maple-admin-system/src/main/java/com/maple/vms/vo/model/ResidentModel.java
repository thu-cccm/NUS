package com.maple.vms.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 农村人口档案展示模型.
 */
@Data
@ApiModel(value = "农村人口档案模型", description = "vms-农村人口档案展示模型")
public class ResidentModel {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "性别(0女,1男)")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "所属村组")
    private Integer groupNo;

    @ApiModelProperty(value = "政治面貌")
    private String politics;

    @ApiModelProperty(value = "是否贫困户(0否,1是)")
    private Integer isPoor;

    @ApiModelProperty(value = "健康状况")
    private String healthStatus;

    @ApiModelProperty(value = "户号")
    private String familyId;

    @ApiModelProperty(value = "是否户主(0否,1是)")
    private Integer isHouseholder;

    @ApiModelProperty(value = "特殊人群标记(JSON数组)")
    private String tags;

    @ApiModelProperty(value = "特殊人群标记列表(前端使用)")
    private List<String> tagList;

    @ApiModelProperty(value = "迁移状态(在村/迁入/迁出)")
    private String migrateStatus;

    @ApiModelProperty(value = "迁出地")
    private String migrateTo;

    @ApiModelProperty(value = "迁出/迁入时间")
    private Date migrateTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}

