package com.maple.rest.config;

import com.maple.common.config.LocalFileProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class LocalFileConfig implements WebMvcConfigurer {

    private final LocalFileProperties localFileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/local/images/**").
                addResourceLocations("file:" + localFileProperties.getImageFilePath());

        registry.addResourceHandler("/fileApi/local/images/**").
                addResourceLocations("file:" + localFileProperties.getImageFilePath());
        
        registry.addResourceHandler("/local/doc/**").
                addResourceLocations("file:" + localFileProperties.getDocFilePath());

        registry.addResourceHandler("/fileApi/local/doc/**").
                addResourceLocations("file:" + localFileProperties.getImageFilePath());
    }
}
