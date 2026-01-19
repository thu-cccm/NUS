package com.maple.common.config.exception;

public enum ErrorCode {

    NO_TOKEN("1001", "用户未登录"),
    TOKEN_EXPIRE("1002", "登录超时，请重新登录"),
    TOKEN_EXCHANGE("1003", "账号在其他地方登录，账号被踢出"),
    USER_LOGIN_ERROR("2001", "用户名或密码错误"),
    USER_STATUS_ERROR("2002", "用户已被停用，请联系管理员"),

    ARTICLE_CHECK_ERROR("3003", "资源不存在"),
    DISABLE_UPDATE_ERROR("9000", "演示环境，不支持增删改操作"),
    PARAM_ERROR("9001", "请求参数有误，请重试"),
    NOT_FIND_DATA("9002", "数据不存在，请刷新后重试"),
    
    COMMON_ERROR("9998", "系统异常，请联系管理员"),
    OTHER_ERROR("9999", "未知异常，请联系管理员");

    private final String code;

    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
