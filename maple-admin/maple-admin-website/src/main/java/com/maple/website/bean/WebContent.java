package com.maple.website.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maple.common.config.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("web_content")
@ApiModel(value = "网站文章内容对象", description = "website-网站文章内容信息表")
public class WebContent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "简介id")
    private Long articleId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "原始内容")
    private String originalContent;

}
