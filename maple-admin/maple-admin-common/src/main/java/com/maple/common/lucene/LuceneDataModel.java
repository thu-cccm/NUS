package com.maple.common.lucene;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuceneDataModel {

    private Long id;

    private Integer type;

    private String title;

    private String description;

    private String content;

    private String imageUrl;

    private String originalUrl;
}
