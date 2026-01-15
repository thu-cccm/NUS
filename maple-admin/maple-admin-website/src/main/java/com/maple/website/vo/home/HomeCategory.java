package com.maple.website.vo.home;

import lombok.Data;

import java.util.List;

@Data
public class HomeCategory {

    private String heading;

    private String description;

    private List<HomeCategoryItem> items;
}
