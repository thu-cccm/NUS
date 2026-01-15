package com.maple.common.model;

import lombok.Data;

@Data
public class ResultJson {

    private static final String SUCCESS_CODE = "0000";

    private Boolean status;

    private String code;

    private String msg;

    private Object data;

    public ResultJson() {
        this.status = true;
        this.code = SUCCESS_CODE;
        this.msg = "";
    }

    public ResultJson(Object data) {
        this.status = true;
        this.code = SUCCESS_CODE;
        this.msg = "";
        this.data = data;
    }

    public ResultJson(String code, String msg) {
        this.status = SUCCESS_CODE.equals(code);
        this.code = code;
        this.msg = msg;
    }

    public ResultJson(Boolean status, String code, String msg, Object data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
