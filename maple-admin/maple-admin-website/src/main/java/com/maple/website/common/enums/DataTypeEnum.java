package com.maple.website.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataTypeEnum {

    ARTICLE(1, "文章"),
    RESOURCE(2, "资源"),
    LINKS(3, "链接"),
    TALK(4, "说说"),
    COMMENT(5, "评论"),
    ;

    private final int code;

    private final String msg;
}
