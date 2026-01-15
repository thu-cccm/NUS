package com.maple.common.config;

public class GlobalConfig {

    private GlobalConfig() {

    }

    public static final long EXPIRE_TIME = 60 * 60 * 12L;

    public static final long APPLET_AUTH_EXPIRE_TIME = 300;

    public static final String SECRET = "maple1223";

    public static String getRedisUserKey(String account) {
        return "MAPLE_BOOT_ADMIN:" + account;
    }

    public static String getAppletRedisUserKey(String account) {
        return "MAPLE_APPLET:" + account;
    }

    public static String getWebRedisUserKey(Long account) {
        return "MAPLE_BOOT_WEB:" + account;
    }

    public static String getAppletAuthRedisKey(String uniCode) {
        return "MAPLE_APPLET_AUTH:" + uniCode;
    }

    public static String getAppletAuthResultRedisKey(String uniCode) {
        return "MAPLE_APPLET_AUTH_RESULT:" + uniCode;
    }

    public static final String TOKEN_NAME = "Authorization";

    public static final Long SIGN_MAPLE_NUM = 20L;
}
