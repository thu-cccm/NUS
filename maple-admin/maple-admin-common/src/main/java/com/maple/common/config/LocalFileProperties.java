package com.maple.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LocalFileProperties {

    @Value("${file.imageFilePath}")
    private String imageFilePath;

    @Value("${file.docFilePath}")
    private String docFilePath;

    @Value("${file.maxFileSize}")
    private long maxFileSize;

}
