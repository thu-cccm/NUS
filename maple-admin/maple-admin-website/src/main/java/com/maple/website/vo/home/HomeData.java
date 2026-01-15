package com.maple.website.vo.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeData {

    private Long articleNum;

    private Long linksNum;

    private Long resourceNum;

    private List<HomeCategory> homeCategoryList;
}
