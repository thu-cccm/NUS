package com.maple.rest.aop;

import com.alibaba.fastjson.JSON;
import com.maple.common.config.GlobalConfig;
import com.maple.common.config.MapleConstants;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.model.ResultJson;
import com.maple.common.util.JwtUtil;
import com.maple.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@WebFilter(filterName = "manageJwtFilter", urlPatterns = {"/*"})
@RequiredArgsConstructor
public class ManageJwtFilter implements Filter {

    private final List<String> includedUrlList;

    private final List<String> excludedUrlList;

    @Value("${maple.isDisableUpdate}")
    private Boolean isDisableUpdate;

    @Override
    public void init(FilterConfig filterConfig) {
        includedUrlList.addAll(Arrays.asList(
                "/manage/*"
        ));
        excludedUrlList.addAll(Arrays.asList(
                "/manage/sso/login",
                "/manage/sso/logout",
                "/manage/blog/initLucene"
        ));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURI();
        boolean isInclude = false;
        boolean isExclude = false;
        for (String excludedUrl : includedUrlList) {
            if (Pattern.matches(excludedUrl.replace("*", ".*"), url)) {
                isInclude = true;
                break;
            }
        }
        for (String excludedUrl : excludedUrlList) {
            if (Pattern.matches(excludedUrl.replace("*", ".*"), url)) {
                isExclude = true;
                break;
            }
        }
        if (!isInclude || isExclude) {
            chain.doFilter(request, response);
        } else {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            
            if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
                chain.doFilter(request, response);
            }
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            RedisUtil redisService = (RedisUtil) factory.getBean("redisUtil");
            String account;
            String authorization = httpServletRequest.getHeader(MapleConstants.TOKEN_NAME);
            
            if (StringUtils.isEmpty(authorization)) {
                writeRsp(httpServletResponse, ErrorCode.NO_TOKEN);
                return;
            } else {
                account = JwtUtil.getAccount(authorization);
                String token = (String) redisService.get(GlobalConfig.getRedisUserKey(account));
                
                if (StringUtils.isEmpty(token)) {
                    writeRsp(httpServletResponse, ErrorCode.TOKEN_EXPIRE);
                    return;
                } else {
                    
                    if (!token.equalsIgnoreCase(authorization)) {
                        writeRsp(httpServletResponse, ErrorCode.TOKEN_EXCHANGE);
                        return;
                    }
                }
            }
            redisService.set(GlobalConfig.getRedisUserKey(account), authorization, GlobalConfig.EXPIRE_TIME);
            
            if (BooleanUtils.isTrue(isDisableUpdate)
                    && BooleanUtils.isNotTrue(JwtUtil.isAdmin())
                    && (httpServletRequest.getRequestURI().contains("create")
                            || httpServletRequest.getRequestURI().contains("update")
                            || httpServletRequest.getRequestURI().contains("upload")
                            || httpServletRequest.getMethod().equals(HttpMethod.DELETE.name()))) {
                writeRsp(httpServletResponse, ErrorCode.DISABLE_UPDATE_ERROR);
                return;
            }
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private void writeRsp(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setHeader("content-type", "application/json;charset=UTF-8");
        try {
            response.getWriter().println(JSON.toJSON(new ResultJson(errorCode.getCode(), errorCode.getMsg())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
