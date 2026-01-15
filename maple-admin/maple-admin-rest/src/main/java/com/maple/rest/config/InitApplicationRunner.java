package com.maple.rest.config;

import com.maple.website.service.IWebArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {

    private final IWebArticleService articleService;

    @Value("${run.initLucene}")
    private Boolean isInitLucene;

    public InitApplicationRunner(IWebArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (Boolean.TRUE.equals(isInitLucene)) {
            log.info("初始化Lucene索引开始");
            articleService.initLuceneData();
            log.info("初始化Lucene索引结束");
        }
    }
}
