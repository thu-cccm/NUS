package com.maple.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    SYSTEM_USER("00", "系统用户"),
    APPLET_USER("01", "小程序用户"),
    ;

    private final String code;

    private final String desc;
}
