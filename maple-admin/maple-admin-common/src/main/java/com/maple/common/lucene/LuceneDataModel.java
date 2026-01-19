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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public static LuceneDataModelBuilder builder() {
        return new LuceneDataModelBuilder();
    }

    public static class LuceneDataModelBuilder {
        private Long id;
        private Integer type;
        private String title;
        private String description;
        private String content;
        private String imageUrl;
        private String originalUrl;

        public LuceneDataModelBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LuceneDataModelBuilder type(Integer type) {
            this.type = type;
            return this;
        }

        public LuceneDataModelBuilder title(String title) {
            this.title = title;
            return this;
        }

        public LuceneDataModelBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LuceneDataModelBuilder content(String content) {
            this.content = content;
            return this;
        }

        public LuceneDataModelBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public LuceneDataModelBuilder originalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
            return this;
        }

        public LuceneDataModel build() {
            LuceneDataModel model = new LuceneDataModel();
            model.id = this.id;
            model.type = this.type;
            model.title = this.title;
            model.description = this.description;
            model.content = this.content;
            model.imageUrl = this.imageUrl;
            model.originalUrl = this.originalUrl;
            return model;
        }
    }
}
