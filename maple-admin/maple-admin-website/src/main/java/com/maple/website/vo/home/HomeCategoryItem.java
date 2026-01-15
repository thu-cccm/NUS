package com.maple.website.vo.home;

import lombok.Data;

@Data
public class HomeCategoryItem {

    private Long id;

    private String image;

    private String title;

    private String subtitle;

    private String route;

    private String description;
}
