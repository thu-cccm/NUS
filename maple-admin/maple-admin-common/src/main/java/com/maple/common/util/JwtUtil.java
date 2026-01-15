package com.maple.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maple.common.config.GlobalConfig;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import com.maple.common.model.TokenBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JwtUtil {

    private static final String IS_ADMIN = "isAdmin";
    private static final String ACCOUNT = "account";
    private static final String USER_ID = "userId";
    private static final String USER_TYPE = "userType";
    private static final String ROLE_ID_LIST = "roleIdList";
    private static final String ROLE_LIST = "roleList";
    private static final String DEPT_ID = "deptId";
    private static final String SESSION_KEY = "sessionKey";
    private static final String OPEN_ID = "openId";

    public static boolean verify(String token, String account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(GlobalConfig.SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(ACCOUNT, account).build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static String getAccount() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getUserId() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(USER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Boolean isAdmin() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(IS_ADMIN).asBoolean();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static TokenBean getTokenMsg() {
        TokenBean tokenBean = new TokenBean();
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return tokenBean;
            }
            tokenBean.setUserId(jwt.getClaim(USER_ID).asLong());
            tokenBean.setAccount(jwt.getClaim(ACCOUNT).asString());
            return tokenBean;

        } catch (JWTDecodeException e) {
            return tokenBean;
        }
    }

    private static DecodedJWT getJwt() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            throw new MapleCheckException(ErrorCode.PARAM_ERROR);
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return null;
        }
        return JWT.decode(authorization);
    }

    public static String getRolesByToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(ROLE_LIST).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getRoleIdList() {
        DecodedJWT jwt = getJwt();
        if (jwt == null) {
            return new ArrayList<>();
        }
        String roleString = jwt.getClaim(ROLE_ID_LIST).asString();
        if (StringUtils.isNotEmpty(roleString)) {
            return Arrays.asList(roleString.split(","));
        }

        return new ArrayList<>();
    }

    public static String getRoleList() {
        DecodedJWT jwt = getJwt();
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim(ROLE_LIST).asString();
    }

    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(GlobalConfig.SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            jwt.getClaims();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getSessionKey() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(SESSION_KEY).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getOpenId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(OPEN_ID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getDeptId() {
        try {
            DecodedJWT jwt = getJwt();
            if (jwt == null) {
                return null;
            }
            return jwt.getClaim(DEPT_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String createToken(TokenBean tokenBean) {
        Algorithm algorithm = Algorithm.HMAC256(GlobalConfig.SECRET);
        return JWT.create().withClaim(USER_ID, tokenBean.getUserId())
                .withClaim(IS_ADMIN, tokenBean.getIsAdmin())
                .withClaim(ACCOUNT, tokenBean.getAccount())
                .withClaim(USER_TYPE, tokenBean.getUserType())
                .withClaim(ROLE_LIST, tokenBean.getRoleList())
                .withClaim(ROLE_ID_LIST, tokenBean.getRoleIdList())
                .withClaim(DEPT_ID, tokenBean.getDeptId())
                .withClaim(SESSION_KEY, tokenBean.getSessionKey())
                .withClaim(OPEN_ID, tokenBean.getOpenId())
                .sign(algorithm);
    }
}
