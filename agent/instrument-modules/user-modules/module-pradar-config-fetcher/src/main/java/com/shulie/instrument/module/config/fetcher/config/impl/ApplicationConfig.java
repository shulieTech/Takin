/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.module.config.fetcher.config.impl;

import com.pamirs.pradar.internal.config.*;
import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;
import com.shulie.instrument.module.config.fetcher.config.resolver.ConfigResolver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用总配置项
 *
 * @author shiyajian
 * @since 2020-08-11
 */
public class ApplicationConfig extends AbstractConfig<ApplicationConfig> {
    public static Boolean getWhiteList = Boolean.FALSE;
    public static Boolean getPressureTable4AccessSimple = Boolean.FALSE;
    public static Boolean getShadowJobConfig = Boolean.FALSE;

    /**
     * war name list
     */
    private Set<String> urlWhiteList;

    /**
     * rabbitmq queue list
     */
    private Set<String> mqList;

    /**
     * 白名单开关
     */
    private boolean allowListSwitchOn;

    /**
     * dubbo 白名单列表
     */
    private Set<String> rpcNameWhiteList;

    /**
     * redis 不允许访问的 key 列表
     */
    private Set<String> contextPathBlockList;

    /**
     * es黑名单
     */
    private Set<String> searchWhiteList;

    /**
     * redis 的 key list
     */
    private Set<String> cacheKeyAllowList;

    /**
     * 影子job配置
     */
    private Set<ShadowJob> shadowJobs;

    /**
     * mock配置
     */
    private Set<MockConfig> mockConfigs;

    private Map<String, ShadowDatabaseConfig> shadowDatabaseConfigs;

    /**
     * redis影子server配置集合
     */
    private Map<String, ShadowRedisConfig> shadowRedisConfigs;

    private Map<String, ShadowEsServerConfig> shadowEsServerConfigs;

    private Map<String, ShadowHbaseConfig> shadowHbaseConfigs;

    public ApplicationConfig(ConfigResolver<ApplicationConfig> resolver) {
        super(resolver);
        this.urlWhiteList = new HashSet<String>();
        this.rpcNameWhiteList = new HashSet<String>();
        this.allowListSwitchOn = true;
        this.contextPathBlockList = new HashSet<String>();
        this.cacheKeyAllowList = new HashSet<String>();
        this.shadowDatabaseConfigs = new ConcurrentHashMap<String, ShadowDatabaseConfig>();
        this.shadowJobs = new HashSet<ShadowJob>();
        this.type = ApplicationConfig.class;
        this.shadowRedisConfigs = new ConcurrentHashMap<String, ShadowRedisConfig>();
        this.shadowEsServerConfigs = new ConcurrentHashMap<String, ShadowEsServerConfig>();
        this.shadowHbaseConfigs = new ConcurrentHashMap<String, ShadowHbaseConfig>();
    }

    @Override
    public void trigger(FIELDS... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }
        ApplicationConfig applicationConfig = (ApplicationConfig) resolver.fetch(fields);
        if (applicationConfig != null) {
            refreshFields(applicationConfig, fields);
        }

    }

    public void refreshFields(ApplicationConfig newConfig, FIELDS... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }
        for (FIELDS field : fields) {
            switch (field) {
                case URL_WHITE_LIST:
                    change(FIELDS.URL_WHITE_LIST, newConfig.getUrlWhiteList());
                    break;
                case DUBBO_ALLOW_LIST:
                    change(FIELDS.DUBBO_ALLOW_LIST, newConfig.getRpcNameWhiteList());
                    break;
                case CONTEXT_PATH_BLOCK_LIST:
                    change(FIELDS.CONTEXT_PATH_BLOCK_LIST, newConfig.getContextPathBlockList());
                    break;
                case CACHE_KEY_ALLOW_LIST:
                    change(FIELDS.CACHE_KEY_ALLOW_LIST, newConfig.getCacheKeyAllowList());
                    break;
                case SHADOW_DATABASE_CONFIGS:
                    change(FIELDS.SHADOW_DATABASE_CONFIGS, newConfig.getShadowDatabaseConfigs());
                    break;
                case SHADOW_REDIS_SERVER_CONFIG:
                    change(FIELDS.SHADOW_REDIS_SERVER_CONFIG, newConfig.getShadowRedisConfigs());
                    break;
                case SHADOW_ES_SERVER_CONFIG:
                    change(FIELDS.SHADOW_ES_SERVER_CONFIG, newConfig.getShadowEsServerConfigs());
                    break;
                case SHADOW_JOB_CONFIGS:
                    change(FIELDS.SHADOW_JOB_CONFIGS, newConfig.getShadowJobs());
                    break;
                case MOCK_CONFIGS:
                    change(FIELDS.MOCK_CONFIGS, newConfig.getMockConfigs());
                    break;
                case SHADOW_HBASE_SERVER_CONFIG:
                    change(FIELDS.SHADOW_HBASE_SERVER_CONFIG, newConfig.getShadowHbaseConfigs());
                    break;
            }
        }
    }

    @Override
    public void refresh(ApplicationConfig newConfig) {
        if (getWhiteList) {
            change(FIELDS.URL_WHITE_LIST, newConfig.getUrlWhiteList());
            change(FIELDS.DUBBO_ALLOW_LIST, newConfig.getRpcNameWhiteList());
            change(FIELDS.CONTEXT_PATH_BLOCK_LIST, newConfig.getContextPathBlockList());
            change(FIELDS.CACHE_KEY_ALLOW_LIST, newConfig.getCacheKeyAllowList());
            change(FIELDS.SEARCH_KEY_WHITE_LIST, newConfig.getSearchWhiteList());
            change(FIELDS.MQ_WHITE_LIST, newConfig.getMqList());
        }
        if (getPressureTable4AccessSimple) {
            change(FIELDS.SHADOW_DATABASE_CONFIGS, newConfig.getShadowDatabaseConfigs());
            change(FIELDS.SHADOW_JOB_CONFIGS, newConfig.getShadowJobs());
            change(FIELDS.MOCK_CONFIGS, newConfig.getMockConfigs());
        }
        change(FIELDS.SHADOW_REDIS_SERVER_CONFIG, newConfig.getShadowRedisConfigs());
        change(FIELDS.SHADOW_ES_SERVER_CONFIG, newConfig.getShadowEsServerConfigs());
    }

    public Set<String> getUrlWhiteList() {
        return urlWhiteList;
    }

    public void setUrlWhiteList(Set<String> urlWhiteList) {
        this.urlWhiteList = urlWhiteList;
    }

    public Set<String> getMqList() {
        return mqList;
    }

    public void setMqList(Set<String> mqList) {
        this.mqList = mqList;
    }


    public boolean isAllowListSwitchOn() {
        return allowListSwitchOn;
    }

    public void setAllowListSwitchOn(boolean allowListSwitchOn) {
        this.allowListSwitchOn = allowListSwitchOn;
    }

    public Set<String> getRpcNameWhiteList() {
        return rpcNameWhiteList;
    }

    public void setRpcNameWhiteList(Set<String> rpcNameWhiteList) {
        this.rpcNameWhiteList = rpcNameWhiteList;
    }

    public Set<String> getContextPathBlockList() {
        return contextPathBlockList;
    }

    public void setContextPathBlockList(Set<String> contextPathBlockList) {
        this.contextPathBlockList = contextPathBlockList;
    }

    public Set<String> getCacheKeyAllowList() {
        return cacheKeyAllowList;
    }

    public void setCacheKeyAllowList(Set<String> redisKeyAllowList) {
        this.cacheKeyAllowList = redisKeyAllowList;
    }

    public Map<String, ShadowDatabaseConfig> getShadowDatabaseConfigs() {
        return shadowDatabaseConfigs;
    }

    public void setShadowDatabaseConfigs(Map<String, ShadowDatabaseConfig> shadowDatabaseConfigs) {
        this.shadowDatabaseConfigs = shadowDatabaseConfigs;
    }

    public Set<String> getSearchWhiteList() {
        return searchWhiteList;
    }

    public void setSearchWhiteList(Set<String> searchWhiteList) {
        this.searchWhiteList = searchWhiteList;
    }

    public Set<ShadowJob> getShadowJobs() {
        return shadowJobs;
    }

    public void setShadowJobs(Set<ShadowJob> shadowJobs) {
        this.shadowJobs = shadowJobs;
    }

    public Set<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(Set<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }

    public Map<String, ShadowRedisConfig> getShadowRedisConfigs() {
        return shadowRedisConfigs;
    }

    public void setShadowRedisConfigs(Map<String, ShadowRedisConfig> shadowRedisConfigs) {
        this.shadowRedisConfigs = shadowRedisConfigs;
    }

    public Map<String, ShadowEsServerConfig> getShadowEsServerConfigs() {
        return shadowEsServerConfigs;
    }

    public void setShadowEsServerConfigs(
        Map<String, ShadowEsServerConfig> shadowEsServerConfigs) {
        this.shadowEsServerConfigs = shadowEsServerConfigs;
    }

    public Map<String, ShadowHbaseConfig> getShadowHbaseConfigs() {
        return shadowHbaseConfigs;
    }

    public void setShadowHbaseConfigs(Map<String, ShadowHbaseConfig> shadowHbaseConfigs) {
        this.shadowHbaseConfigs = shadowHbaseConfigs;
    }
}
