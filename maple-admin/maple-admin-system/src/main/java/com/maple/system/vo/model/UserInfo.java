package com.maple.system.vo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo {

    private UserModel userModel;

    private Set<String> roles;

    private Set<String> permissions;
}
