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

import io.shulie.tro.web.app.conf.intercepter.AgentInterceptor;
import io.shulie.tro.web.app.conf.intercepter.LoginInterceptor;
import io.shulie.tro.web.app.conf.intercepter.MenuAuthInterceptor;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.AgentUrls;
import io.shulie.tro.web.app.constant.PressureMachineAgentUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shulie
 * @Date: 2019/9/20 15:14
 * @Description:
 */
@Configuration
public class CustomMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private MenuAuthInterceptor menuAuthInterceptor;

    @Autowired
    private AgentInterceptor agentInterceptor;

    /**
     * 跨域支持
     */
    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600 * 24L);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
            .excludePathPatterns("/login/**")
            .excludePathPatterns("/api/verification/code")
            .excludePathPatterns("/**/api/loginmanage/login")
            .excludePathPatterns("/**/api/user/login")
            .excludePathPatterns("/**/api/login/**")
            .excludePathPatterns("/**/api/loginNoCode")
            .excludePathPatterns("/**/api/logout/**")
            .excludePathPatterns("/**/doc.html/**")
            .excludePathPatterns("/**/swagger-ui.html/**")
            .excludePathPatterns("/**/webjars/**")
            .excludePathPatterns("/**/swagger-resources/**")
            .excludePathPatterns("/**/api-docs/**")
            .excludePathPatterns("/", "/css/**", "/js/**", "/img/**")
            .excludePathPatterns("/**/api/collector/**")
            .excludePathPatterns("/**/api/api/pull")
            .excludePathPatterns("/**/api/noauth/**")
            .excludePathPatterns("/**/api/health")
            .excludePathPatterns("/**/application/center/app/config/export")
            .excludePathPatterns("/**/api/user/download")
            .excludePathPatterns("/**/api/user/example/download")
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.AGENT_VERSION)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_INSERT_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_PRESSURE_SWITCH_STATUS)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_WHITE_LIST_SWITCH_STATUS)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.BWLISTMETRIC_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.GUARD_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.MIDDLE_STAUTS_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.REGISTER_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.ROCKETMQ_ISO_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_DB_TABLE_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_ES_SERVER_URL)
            // kafka
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_KAFKA_CLUSTER_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_HBASE_SERVER_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.TRO_REPORT_ERROR_SHADOW_JOB_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.TRO_SHADOW_JOB_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD_ACCESS_STATUS)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD_APP_INFO)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.WHITELIST_FILE_URL + "/**")
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.WHITELIST_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_SERVER_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.PERFORMANCE_BASE_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.PERFORMANCE_TRACE_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.FAST_DEBUG_STACK_UPLOAD_URL)
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.FAST_DEBUG_AGENT_LOG_UPLOAD_URL)
            .excludePathPatterns("/**/" + PressureMachineAgentUrls.PRESSURE_MACHINE_UPLOAD)
            .excludePathPatterns("/**" + APIUrls.TRO_OPEN_API_URL + "**")
            .excludePathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_SHADOW_CONSUMER_URL)
        ;

        registry.addInterceptor(agentInterceptor)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.AGENT_VERSION)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_INSERT_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_PRESSURE_SWITCH_STATUS)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.APP_WHITE_LIST_SWITCH_STATUS)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.BWLISTMETRIC_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.GUARD_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.MIDDLE_STAUTS_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.REGISTER_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.ROCKETMQ_ISO_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_DB_TABLE_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_ES_SERVER_URL)
            // kafka
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_KAFKA_CLUSTER_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_HBASE_SERVER_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.TRO_REPORT_ERROR_SHADOW_JOB_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.TRO_SHADOW_JOB_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD_ACCESS_STATUS)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.UPLOAD_APP_INFO)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.WHITELIST_FILE_URL + "/**")
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.WHITELIST_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.FAST_DEBUG_AGENT_LOG_UPLOAD_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.FAST_DEBUG_STACK_UPLOAD_URL)
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_SERVER_URL + "/**")
            .addPathPatterns("/**/" + AgentUrls.PREFIX_URL + AgentUrls.SHADOW_SHADOW_CONSUMER_URL)
        ;

        registry.addInterceptor(menuAuthInterceptor)
            .addPathPatterns("/**/api/user/work/bench")
            .addPathPatterns("/**/api/activities")
            .addPathPatterns("/**/api/link/scene/manage")
            .addPathPatterns("/**/api/application/center/list")
            .addPathPatterns("/**/api/scenemanage/list")
            .addPathPatterns("/**/api/report/listReport")
            .addPathPatterns("/**/api/application/center/app/switch")
            .addPathPatterns("/**/api/console/switch/whitelist")
            .addPathPatterns("/**/api/confcenter/query/blist")
            .addPathPatterns("/**/api/api/get")
            .addPathPatterns("/**/api/settle/balance/list")
            .addPathPatterns("/**/api/operation/log/list")
            .addPathPatterns("/**/api/role/list")
            .addPathPatterns("/**/api/scriptManage")
            .addPathPatterns("/**/api/pradar/switch/list");
    }
}
