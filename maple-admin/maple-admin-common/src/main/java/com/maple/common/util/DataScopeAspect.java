package com.maple.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maple.common.config.bean.BaseQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class DataScopeAspect {

    public static final String DATA_SCOPE_ALL = "1";

    public static final String DATA_SCOPE_CUSTOM = "2";

    public static final String DATA_SCOPE_DEPT = "3";

    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    public static final String DATA_SCOPE_SELF = "5";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        
        if (Objects.nonNull(JwtUtil.getUserId()) && BooleanUtils.isNotTrue(JwtUtil.isAdmin())) {
            dataScopeFilter(joinPoint, controllerDataScope.deptAlias(), controllerDataScope.userAlias());
        }
    }

    public static void dataScopeFilter(JoinPoint joinPoint, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        for (JSONObject role : Objects.requireNonNull(JSON.parseArray(JwtUtil.getRoleList(), JSONObject.class))) {
            String dataScope = role.getString("dataScope");
            Long roleId = role.getLong("id");
            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope)) {
                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(String.format(
                        " OR %s IN ( SELECT dept_id FROM usc_role_dept WHERE role_id = %d ) ", deptAlias, roleId));
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(String.format(" OR %s = %d ", deptAlias, JwtUtil.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(String.format(
                        " OR %s IN ( SELECT id FROM usc_dept WHERE id = %d or find_in_set( %d , ancestors ) )",
                        deptAlias, JwtUtil.getDeptId(), JwtUtil.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(String.format(" OR %s = %d ", userAlias, JwtUtil.getUserId()));
                } else {
                    
                    sqlString.append(String.format(" OR %s = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }

        if (CollectionUtils.isEmpty(conditions)) {
            sqlString.append(String.format(" OR %s = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (Objects.nonNull(params) && params instanceof BaseQuery) {
                BaseQuery baseQuery = (BaseQuery) params;
                
                baseQuery.setDataScope(" AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (Objects.nonNull(params) && params instanceof BaseQuery) {
            BaseQuery baseQuery = (BaseQuery) params;
            baseQuery.setDataScope("");
        }
    }
}