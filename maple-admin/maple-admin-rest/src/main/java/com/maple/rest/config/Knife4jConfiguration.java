package com.maple.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "adminManage")
    public Docket adminManage() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("管理平台接口")
                        .description("管理平台接口")
                        .termsOfServiceUrl("https:locahost:8080")
                        .contact(new Contact( "maoshun tian","https:locahost:8080","maoshun.tian@gmail.com"))
                        .version("1.0")
                        .build())

                .groupName("管理平台接口")
                .select()

                .apis(RequestHandlerSelectors.basePackage("com.maple.rest.controller.manage"))
                .paths(PathSelectors.any())
                .build();
    }
}
