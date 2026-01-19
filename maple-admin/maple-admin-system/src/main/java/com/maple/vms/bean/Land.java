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

/**
 * 土地资源实体.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vms_land")
@ApiModel(value = "土地资源对象", description = "vms-土地资源管理信息表")
public class Land implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关联户主ID")
    private Long residentId;

    @ApiModelProperty(value = "地块编号")
    private String landCode;

    @ApiModelProperty(value = "面积(亩)")
    private BigDecimal area;

    @ApiModelProperty(value = "土地类型")
    private String type;

    @ApiModelProperty(value = "地理位置描述")
    private String location;

    @ApiModelProperty(value = "确权状态(0待确权,1已确权)")
    private Integer status;

    @ApiModelProperty(value = "确权证书图片路径")
    private String certUrl;
}

