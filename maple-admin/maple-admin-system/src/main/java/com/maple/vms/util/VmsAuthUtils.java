package com.maple.vms.util;

import com.alibaba.fastjson.JSON;
import com.maple.common.util.JwtUtil;
import com.maple.system.bean.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 新农村模块权限工具类.
 */
public class VmsAuthUtils {

    private VmsAuthUtils() {
    }

    /**
     * 判断当前用户是否为村民角色.
     *
     * @return 是否村民
     */
    public static boolean isVillager() {
        String roleList = JwtUtil.getRoleList();
        if (StringUtils.isBlank(roleList)) {
            return false;
        }
        List<Role> roles = JSON.parseArray(roleList, Role.class);
        return roles.stream().anyMatch(role -> "villager".equalsIgnoreCase(role.getRoleKey()));
    }
}

