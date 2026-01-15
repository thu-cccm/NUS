package com.maple.common.config.exception;

public class MapleCommonException extends MapleBaseException {

    public MapleCommonException(String code, String errorMsg) {
        super(code, errorMsg);
    }

    public MapleCommonException(ErrorCode code) {
        super(code);
    }

    public MapleCommonException(ErrorCode code, String errorMsg) {
        super(code, errorMsg);
    }
}
