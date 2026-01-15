package com.maple.rest.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.maple.system.bean.OperateLog;
import com.maple.system.mapper.OperateLogMapper;
import com.maple.common.config.MapleConstants;
import com.maple.common.model.MapleLog;
import com.maple.common.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

import static com.maple.common.config.MapleConstants.MAX_TEXT_LENGTH;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class SystemLogAspect {

    private final OperateLogMapper operateLogMapper;

    @Pointcut(value = "@annotation(com.maple.common.model.MapleLog)")
    public void systemLog() {
    }

    @Around(value = "systemLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = new Object();
        
        long startTime;
        
        long endTime;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        MapleLog mapleLog = method.getAnnotation(MapleLog.class);
        OperateLog operateLog = new OperateLog();

        try {
            operateLog.setBrowser(request.getHeader("USER-AGENT"));
            operateLog.setOperateUrl(request.getRequestURI());
            operateLog.setRequestMethod(request.getMethod());
            operateLog.setMethod(String.valueOf(joinPoint.getSignature()));
            operateLog.setCreateTime(new Date());
            operateLog.setOperateIp(getIpAddress(request));
            
            if (request.getHeader(MapleConstants.TOKEN_NAME) != null) {
                operateLog.setCreateId(JwtUtil.getUserId());
            }
            String operateParam = JSON.toJSONStringWithDateFormat(joinPoint.getArgs(), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
            if (operateParam.length() > MAX_TEXT_LENGTH) {
                operateParam = operateParam.substring(0, MAX_TEXT_LENGTH);
            }
            operateLog.setOperateParam(operateParam);

            if (apiOperation != null) {
                operateLog.setTitle(apiOperation.value() + "");
            }

            if (mapleLog != null) {
                operateLog.setBusinessType(mapleLog.businessType().ordinal());
                operateLog.setOperateType(mapleLog.operateType().ordinal());
                if (StringUtils.isNotBlank(mapleLog.value())) {
                    operateLog.setTitle(mapleLog.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        try {
            obj = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            operateLog.setRespTime(endTime - startTime);
            operateLog.setStatus(0);
            
            if (mapleLog.saveResult()) {
                String result = JSON.toJSONString(obj);
                if (result.length() > MAX_TEXT_LENGTH) {
                    result = result.substring(0, MAX_TEXT_LENGTH);
                }
                operateLog.setJsonResult(result);
            }
        } catch (Exception e) {
            
            operateLog.setStatus(1);
            operateLog.setErrorMsg(e.toString());
            throw e;
        } finally {
            endTime = System.currentTimeMillis();
            operateLog.setRespTime(endTime - startTime);
            operateLogMapper.insert(operateLog);
        }
        return obj;
    }

    private static String getIpAddress(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (StringUtils.isNotEmpty(xFor) && !unknown.equalsIgnoreCase(xFor)) {
            
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xip;
        if (StringUtils.isNotEmpty(xFor) && !unknown.equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || unknown.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }
}
