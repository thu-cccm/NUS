package com.maple.website.vo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppletAuthModel {

    @ApiModelProperty("请求唯一编码")
    private String uniCode;

    @ApiModelProperty("授权token")
    private String authToken;

    @ApiModelProperty("授权结果")
    private Boolean authResult;

    @ApiModelProperty("用户Token")
    private String token;
}
