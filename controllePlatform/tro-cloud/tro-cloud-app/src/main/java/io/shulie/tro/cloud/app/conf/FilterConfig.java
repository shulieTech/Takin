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

import javax.servlet.Filter;

import io.shulie.tro.cloud.app.filter.BigfileUploadFilter;
import io.shulie.tro.cloud.app.filter.LogTraceIdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: vernon
 * @Date: 2019/12/2 15:34
 * @Description:添加put请求过滤器
 */
@Configuration
public class FilterConfig {

    @Autowired
    private BigfileUploadFilter bigfileUploadFilter;

    @Bean
    public FilterRegistrationBean pufFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
        registration.addUrlPatterns("/*");
        registration.setFilter(new org.springframework.web.filter.HttpPutFormContentFilter());
        registration.setName("httpPutFormContentFilter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<LogTraceIdFilter> logTraceIdFilter() {
        FilterRegistrationBean<LogTraceIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogTraceIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        registrationBean.setName("logTraceIdFilter");
        return registrationBean;
    }

}
