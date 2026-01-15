package com.maple.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenBean {

    private Boolean isAdmin;

    private Long userId;

    private String openId;

    private String sessionKey;

    private Long deptId;

    private String account;

    private String userType;

    private String roleIdList;

    private String roleList;
}
