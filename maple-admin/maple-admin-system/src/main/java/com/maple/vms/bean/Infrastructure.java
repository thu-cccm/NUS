package com.maple.vms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础设施建设实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_infrastructure")
@ApiModel(value = "基础设施对象", description = "vms-基础设施建设信息表")
public class Infrastructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目预算")
    private BigDecimal budget;

    @ApiModelProperty(value = "开工日期")
    private Date startDate;

    @ApiModelProperty(value = "施工单位")
    private String contractor;

    @ApiModelProperty(value = "工程进度(0-100)")
    private Integer progress;

    @ApiModelProperty(value = "状态")
    private String status;
}

