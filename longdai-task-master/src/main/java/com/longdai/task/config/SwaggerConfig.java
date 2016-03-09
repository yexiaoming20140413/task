package com.longdai.task.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * <p>
 * Contributors:
 * DonQuixote  on 3/5/16 - 11:26 AM
 *******************************************************************************/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * api doc -- springfox swagger configuration
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

    @Bean
    public Docket swaggerSpringfoxDocket() {
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("longdai-task-master")
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(".*")) // and by paths
                .build()
                .apiInfo(apiInfo());
        return swaggerSpringMvcPlugin;
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("longdai task master",
                "定时任务管理接口",
                "V1",
                "",
                "longdai.com",
                "MIT License",
                "/LICENSE");
        return apiInfo;
    }

}
