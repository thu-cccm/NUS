package com.maple.common.enums;

public enum UserTypeEnum {

    SYSTEM_USER("00", "系统用户"),
    APPLET_USER("01", "小程序用户"),
    ;

    private final String code;

    private final String desc;

    UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
