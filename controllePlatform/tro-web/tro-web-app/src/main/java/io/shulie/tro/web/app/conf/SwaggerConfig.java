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

package io.shulie.tro.web.app.conf;

import java.time.LocalDate;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import io.shulie.tro.web.app.constant.APIUrls;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
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
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket apiV41() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.1")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(scenemanage|scene|settle|report|link/dictionary).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV411() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.1.1")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/confcenter/wbmnt/(add|query|delete|update)/(blist|blistbyid).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV42() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.2.0")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            //  .paths(PathSelectors.regex("/api/user/work/(bench|bench/access).*"))
            .paths(PathSelectors.regex("/api/(agent|api|link).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV423() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.2.3")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            //  .paths(PathSelectors.regex("/api/user/work/(bench|bench/access).*"))
            .paths(PathSelectors.regex("/api/(global/switch|report/link|report/realtime|application/whitelist).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV430() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.3.0")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            //  .paths(PathSelectors.regex("/api/user/work/(bench|bench/access).*"))
            .paths(PathSelectors.regex("/api/auth.*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV440() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.4.0")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/operation.*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }




    @Bean
    public Docket apiV461() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.6.1")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(scriptManage|scenemanage).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }


    @Bean
    public Docket apiV470() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.7.0")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(leak|datasource).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }
    @Bean
    public Docket apiV471() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.7.1")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(fastdebug|fast/debug).*"))
            .paths(Predicates.not(PathSelectors.regex("/api/fastdebug/debug/callStack/(exception|node/locate).*\"")))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiV480() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-V4.8.0")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(application/names|application/entrances|activities).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiBlacklist() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-新版黑名单")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.ant("/api/confcenter/blacklist/**"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiWhitelist() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("日常迭代-白名单新增接口20210414")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(whitelist/list|application/part|application/global)/*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket apiLink4() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("链路梳理第4期")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(consumers|application/whitelist).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket accountManage() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("日常需求-3.11迭代")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/api/(user|dept).*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket api_openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("压测平台-开放接口v01")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex(APIUrls.TRO_OPEN_API_URL + ".*"))
            .build()
            .directModelSubstitute(LocalDate.class, String.class)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            ;
    }

    @Bean
    public Docket api_FastDebug2() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("tro-web-快速使用2期")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors
                .regex(
                    "/api/(application/center/app/config|datasource|fastdebug/debug/callStack/node/locate|"
                        + "fastdebug/debug/callStack/exception|link/ds/manage).*"))
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
