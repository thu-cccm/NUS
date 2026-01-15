package com.maple.system.vo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaModel {

    private String title;

    private String icon;

    private String isLink;

    private Boolean isHide;

    private Boolean isKeepAlive;

    private Boolean isIframe;

    private Boolean isAffix;
}
