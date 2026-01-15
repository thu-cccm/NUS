package com.maple.common.util.excel.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {

    private String className;

    private String userName;

    private String sex;

    private Integer age;

    private Integer scope;

    private String remark;

    private String remark2;
}
