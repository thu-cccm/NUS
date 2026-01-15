package com.maple.system.vo.model;

import lombok.Data;

import java.util.List;

@Data
public class RouterModel {

    private com.maple.system.vo.model.MetaModel meta;

    private String component;

    private String name;

    private String path;

    private String redirect;

    private List<RouterModel> children;
}
