package com.maple.common.util;

import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class Md5Util {

    private Md5Util() {

    }

    public static String encrypt(String password, String salt) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)) {
            log.error("密码加密失败原因： password and salt cannot be empty");
            throw new MapleCommonException(ErrorCode.PARAM_ERROR);
        }
        return DigestUtils.md5DigestAsHex((salt + password).getBytes());
    }

    public static boolean verifyPassword(String target, String source, String salt) {
        if (StringUtils.isEmpty(target) || StringUtils.isEmpty(source) || StringUtils.isEmpty(salt)) {
            log.info("校验密码失败，原因 target ={}， source ={}， salt ={}", target, source, salt);
            return false;
        }
        String targetEncryptPwd = encrypt(target, salt);
        return targetEncryptPwd.equals(source);
    }
}
