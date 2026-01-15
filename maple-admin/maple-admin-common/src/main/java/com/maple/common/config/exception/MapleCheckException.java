package com.maple.common.config.exception;

public class MapleCheckException extends MapleBaseException {

    public MapleCheckException(String code, String errorMsg) {
        super(code, errorMsg);
    }

    public MapleCheckException(ErrorCode code) {
        super(code);
    }

    public MapleCheckException(ErrorCode code, String errorMsg) {
        super(code, errorMsg);
    }
}
