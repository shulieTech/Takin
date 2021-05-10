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
package com.pamirs.pradar.pressurement.agent.shared.service;

import com.pamirs.pradar.internal.adapter.JobAdapter;
import com.pamirs.pradar.internal.config.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 所有的全局共享配置
 */
public final class GlobalConfig {

    private static GlobalConfig INSTANCE;

    /**
     * debug detail info
     */
    private Set<String> wrongSqlDetail = new HashSet<String>();

    /**
     * war name list
     */
    private Set<String> urlWhiteList = new HashSet<String>();

    /**
     * rpc name list，包含 dubbo、grpc 等
     */
    private Set<String> rpcNameWhiteList = new HashSet<String>();


    /**
     * context path block list
     */
    private Set<String> contextPathBlockList = new HashSet<String>();

    /**
     * 搜索白名单，在搜索白名单中则可以走业务索引，但是只有只读操作，写操作不允许
     */
    private Set<String> searchWhiteList = new HashSet<String>();

    /**
     * redis key list
     */
    private Set<String> cacheKeyWhiteList = new HashSet<String>();

    /**
     * mq 白名单
     */
    private Set<String> mqWhiteList = new HashSet<String>();

    /**
     * 所有的入口规则
     */
    private Set<String> traceRules = new HashSet<String>();
    /**
     * 影子库表的配置
     * key: 不带参数的url#username
     */
    private Map<String, ShadowDatabaseConfig> shadowDatabaseConfigs = new ConcurrentHashMap<String, ShadowDatabaseConfig>();
    private Map<String, ShadowRedisConfig> shadowRedisConfigs = new ConcurrentHashMap<String, ShadowRedisConfig>();
    private Map<String, ShadowEsServerConfig> shadowEsServerConfigs = new ConcurrentHashMap<String, ShadowEsServerConfig>();
    public static Map<String, ShadowHbaseConfig> shadowHbaseServerConfigs = new ConcurrentHashMap<String, ShadowHbaseConfig>();


    //应用启动特定埋点状态信息，如数据库启动对应影子库数据源加入应用是否正常
    private Map<String, String> applicationAccessStatus = new ConcurrentHashMap<String, String>();
    private Map<String, JobAdapter> jobAdapterMap = new HashMap<String, JobAdapter>(5);
    private Map<String, ShadowJob> registerdJobs = new ConcurrentHashMap<String, ShadowJob>();
    private Map<String, ShadowJob> needRegisterJobs = new ConcurrentHashMap<String, ShadowJob>(8, 1);
    private Map<String, ShadowJob> needStopJobs = new ConcurrentHashMap<String, ShadowJob>(8, 1);
    private Set<ShadowJob> errorRegister = new HashSet<ShadowJob>();
    private Set<MockConfig> mockConfigs = new HashSet<MockConfig>();

    /**
     * redis影子库表 默认为影子表
     */
    private volatile boolean isShadowDbRedisServer = false;

    private volatile boolean isShadowEsServer = Boolean.FALSE;

    private volatile boolean isShadowHbaseServer = Boolean.FALSE;

    private Map<String, Set<String>> shadowTable = new HashMap<String, Set<String>>();

    private Set<String> apis = new HashSet<String>();

    private GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (GlobalConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GlobalConfig();
                }
            }
        }
        return INSTANCE;
    }

    public Set<String> getApis() {
        return apis;
    }

    public void setApis(Set<String> apis) {
        this.apis = apis;
    }

    public boolean isShadowDbRedisServer() {
        return isShadowDbRedisServer;
    }

    public void setShadowDbRedisServer(boolean isShadowDbRedisServer) {
        this.isShadowDbRedisServer = isShadowDbRedisServer;
    }

    public Set<MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(Set<MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }

    public Set<String> getWrongSqlDetail() {
        return wrongSqlDetail;
    }

    public void addWrongSqlDetail(String sql) {
        wrongSqlDetail.add(sql);
    }

    public void setWrongSqlDetail(Set<String> wrongSqlDetail) {
        this.wrongSqlDetail = wrongSqlDetail;
    }

    public Map<String, Set<String>> getShadowTables() {
        return shadowTable;
    }

    public void addShadowTable(String url, Set<String> tables) {
        shadowTable.put(url, tables);
    }

    public Set<ShadowJob> getErrorRegisterJobs() {
        return errorRegister;
    }

    public void addErrorRegisteredJob(ShadowJob shadowJob) {
        errorRegister.add(shadowJob);
    }

    public void clearErrorRegisteredJobs() {
        errorRegister.clear();
    }

    public Map<String, ShadowJob> getRegisteredJobs() {
        return registerdJobs;
    }

    public void addRegisteredJob(ShadowJob shadowJob) {
        registerdJobs.put(shadowJob.getClassName(), shadowJob);
    }

    public Map<String, ShadowJob> getNeedRegisterJobs() {
        return needRegisterJobs;
    }

    public void addNeedRegisterJobs(ShadowJob shadowJob) {
        if (needRegisterJobs.size() > 32) {
            needRegisterJobs.clear();
        }
        needRegisterJobs.put(shadowJob.getClassName(), shadowJob);
    }


    public Map<String, ShadowJob> getNeedStopJobs() {
        return needStopJobs;
    }

    public void addNeedStopJobs(ShadowJob shadowJob) {
        if (needStopJobs.size() > 32) {
            needStopJobs.clear();
        }
        needStopJobs.put(shadowJob.getClassName(), shadowJob);
    }


    public void removeRegisteredJob(ShadowJob shadowJob) {
        registerdJobs.remove(shadowJob.getClassName());
    }

    public Map<String, String> getApplicationAccessStatus() {
        return applicationAccessStatus;
    }

    public void addApplicationAccessStatus(String key, String value) {
        applicationAccessStatus.put(key, value);
    }

    public Map<String, JobAdapter> getJobAdaptors() {
        return jobAdapterMap;
    }

    public void addJobAdaptor(String name, JobAdapter jobAdapter) {
        jobAdapterMap.put(name, jobAdapter);
    }

    public JobAdapter getJobAdaptor(String name) {
        return jobAdapterMap.get(name);
    }

    public Map<String, ShadowDatabaseConfig> getShadowDatasourceConfigs() {
        return shadowDatabaseConfigs;
    }

    public boolean containsShadowDatabaseConfig(String key) {
        return shadowDatabaseConfigs.containsKey(key);
    }

    public ShadowDatabaseConfig getShadowDatabaseConfig(String key) {
        return shadowDatabaseConfigs.get(key);
    }

    public boolean isEmptyShadowDatabaseConfigs() {
        return shadowDatabaseConfigs == null || shadowDatabaseConfigs.isEmpty();
    }

    public void setShadowDatabaseConfigs(Map<String, ShadowDatabaseConfig> map) {
        clearShadowDatasourceConfigs();
        this.shadowDatabaseConfigs.putAll(map);
    }

    public void clearShadowDatasourceConfigs() {
        shadowDatabaseConfigs.clear();
    }

    public Map<String, ShadowRedisConfig> getShadowRedisConfigs() {
        return shadowRedisConfigs;
    }

    public void setShadowRedisConfigs(Map<String, ShadowRedisConfig> map) {
        shadowRedisConfigs.clear();
        shadowRedisConfigs.putAll(map);
    }

    public ShadowRedisConfig getShadowRedisConfig(String key) {
        return shadowRedisConfigs.get(key);
    }

    public Set<String> getTraceRules() {
        return traceRules;
    }

    public void setTraceRules(Set<String> traceRules) {
        this.traceRules = traceRules;
    }

    /**
     * 返回搜索白名单，在搜索白名单中则可以走业务索引，但是只有只读操作，写操作不允许
     *
     * @return
     */
    public Set<String> getSearchWhiteList() {
        return searchWhiteList;
    }

    public void setSearchWhiteList(Set<String> searchWhiteList) {
        this.searchWhiteList = searchWhiteList;
    }

    public Set<String> getCacheKeyWhiteList() {
        return cacheKeyWhiteList;
    }

    public void setCacheKeyWhiteList(Set<String> cacheKeyWhiteList) {
        this.cacheKeyWhiteList = cacheKeyWhiteList;
    }

    public Set<String> getMqWhiteList() {
        return mqWhiteList;
    }

    public void setMqWhiteList(Set<String> mqWhiteList) {
        this.mqWhiteList = mqWhiteList;
    }

    public Set<String> getContextPathBlockList() {
        return contextPathBlockList;
    }

    public void setContextPathBlockList(Set<String> contextPathBlockList) {
        this.contextPathBlockList = contextPathBlockList;
    }

    public Set<String> getUrlWhiteList() {
        return urlWhiteList;
    }


    public void setUrlWhiteList(Set<String> urlWhiteList) {
        this.urlWhiteList = urlWhiteList;
    }


    public Set<String> getRpcNameWhiteList() {
        return rpcNameWhiteList;
    }

    public void setRpcNameWhiteList(Set<String> rpcNameWhiteList) {
        this.rpcNameWhiteList = rpcNameWhiteList;
    }

    public Map<String, ShadowEsServerConfig> getShadowEsServerConfigs() {
        return shadowEsServerConfigs;
    }

    public Map<String, ShadowHbaseConfig> getShadowHbaseServerConfigs() {
        return shadowHbaseServerConfigs;
    }

    public void setShadowHbaseServerConfigs(Map<String, ShadowHbaseConfig> shadowHbaseServerConfigs) {
        GlobalConfig.shadowHbaseServerConfigs = shadowHbaseServerConfigs;
    }

    public void setShadowEsServerConfigs(
        Map<String, ShadowEsServerConfig> shadowEsServerConfigs) {
        this.shadowEsServerConfigs = shadowEsServerConfigs;
    }

    public boolean isShadowEsServer() {
        return isShadowEsServer;
    }

    public boolean isShadowHbaseServer() {
        return isShadowHbaseServer;
    }

    public void setShadowHbaseServer(boolean shadowHbaseServer) {
        isShadowHbaseServer = shadowHbaseServer;
    }

    public void setShadowEsServer(boolean shadowEsServer) {
        isShadowEsServer = shadowEsServer;
    }
}
