package com.maple.website.vo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SignInQuery {

    @ApiModelProperty("查询开始时间")
    private Date startDate;

    @ApiModelProperty("查询结束时间")
    private Date endDate;

    private Long userId;
}
