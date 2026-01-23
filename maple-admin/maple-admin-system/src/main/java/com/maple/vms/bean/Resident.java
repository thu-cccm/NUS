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
 * 农村人口档案实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_resident")
@ApiModel(value = "农村人口档案对象", description = "vms-农村人口档案信息表")
public class Resident implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "户号(同一户号表示同一家庭)")
    private String familyId;

    @ApiModelProperty(value = "是否户主(0否,1是)")
    private Integer isHouseholder;

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

    @ApiModelProperty(value = "特殊人群标记(JSON数组)")
    private String tags;

    @ApiModelProperty(value = "迁移状态(在村/迁入/迁出)")
    private String migrateStatus;

    @ApiModelProperty(value = "迁出地")
    private String migrateTo;

    @ApiModelProperty(value = "迁出/迁入时间")
    private Date migrateTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

