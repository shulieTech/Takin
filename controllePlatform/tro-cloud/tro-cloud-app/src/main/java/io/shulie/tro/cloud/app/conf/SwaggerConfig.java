/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.app.conf;

import java.time.LocalDate;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Swagger config.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("默认模块")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.any())
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket api1() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("接入简化")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/shadow.*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("POC")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/poc.*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV4() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(report|scenemanage|file|link/white/list/wlist|scene/task).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV41() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.1")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex(
                "/api/(scenemanage/list|user/list|schedulerecord|strategyconfig|settle|base/config|cloud|machine).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV42() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("4.2.2-压测报告升级")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(report).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    /**
     * api info
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("TRO API Document")
            .build();
    }

}
