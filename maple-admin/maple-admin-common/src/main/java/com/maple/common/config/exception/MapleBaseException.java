package com.maple.common.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MapleBaseException extends RuntimeException {

    private final String code;

    private final String errorMsg;

    public MapleBaseException(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public MapleBaseException(ErrorCode code) {
        super(code.getMsg());
        this.code = code.getCode();
        this.errorMsg = code.getMsg();
    }

    public MapleBaseException(ErrorCode code, String errorMsg) {
        super(errorMsg);
        this.code = code.getCode();
        this.errorMsg = errorMsg;
    }
}
