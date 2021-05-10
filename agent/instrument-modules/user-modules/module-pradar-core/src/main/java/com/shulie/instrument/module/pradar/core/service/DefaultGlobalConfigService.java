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
package com.shulie.instrument.module.pradar.core.service;

import com.pamirs.pradar.internal.IGlobalConfigService;
import com.pamirs.pradar.internal.adapter.JobAdapter;
import com.pamirs.pradar.internal.config.MockConfig;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.internal.config.ShadowJob;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;

import java.util.Map;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/8 10:30 下午
 */
public class DefaultGlobalConfigService implements IGlobalConfigService {
    @Override
    public Set<String> getApis() {
        return GlobalConfig.getInstance().getApis();
    }

    @Override
    public boolean isShadowDbRedisServer() {
        return GlobalConfig.getInstance().isShadowDbRedisServer();
    }

    @Override
    public Set<MockConfig> getMockConfigs() {
        return GlobalConfig.getInstance().getMockConfigs();
    }

    @Override
    public Set<String> getWrongSqlDetail() {
        return GlobalConfig.getInstance().getWrongSqlDetail();
    }

    @Override
    public void addWrongSqlDetail(String sql) {
        GlobalConfig.getInstance().addWrongSqlDetail(sql);
    }

    @Override
    public Map<String, Set<String>> getShadowTables() {
        return GlobalConfig.getInstance().getShadowTables();
    }

    @Override
    public Set<ShadowJob> getErrorRegisterJobs() {
        return GlobalConfig.getInstance().getErrorRegisterJobs();
    }

    @Override
    public void addErrorRegisteredJob(ShadowJob shadowJob) {
        GlobalConfig.getInstance().addErrorRegisteredJob(shadowJob);
    }

    @Override
    public void clearErrorRegisteredJobs() {
        GlobalConfig.getInstance().clearErrorRegisteredJobs();
    }

    @Override
    public Map<String, ShadowJob> getRegisteredJobs() {
        return GlobalConfig.getInstance().getRegisteredJobs();
    }

    @Override
    public void addRegisteredJob(ShadowJob shadowJob) {
        GlobalConfig.getInstance().addRegisteredJob(shadowJob);
    }

    @Override
    public Map<String, ShadowJob> getNeedRegisterJobs() {
        return GlobalConfig.getInstance().getNeedRegisterJobs();
    }

    @Override
    public Map<String, ShadowJob> getNeedStopJobs() {
        return GlobalConfig.getInstance().getNeedStopJobs();
    }

    @Override
    public void addNeedStopJobs(ShadowJob shadowJob) {
        GlobalConfig.getInstance().addNeedStopJobs(shadowJob);
    }

    @Override
    public void removeRegisteredJob(ShadowJob shadowJob) {
        GlobalConfig.getInstance().removeRegisteredJob(shadowJob);
    }

    @Override
    public Map<String, JobAdapter> getJobAdaptors() {
        return GlobalConfig.getInstance().getJobAdaptors();
    }

    @Override
    public void addJobAdaptor(String name, JobAdapter jobAdapter) {
        GlobalConfig.getInstance().addJobAdaptor(name, jobAdapter);
    }

    @Override
    public JobAdapter getJobAdaptor(String name) {
        return GlobalConfig.getInstance().getJobAdaptor(name);
    }

    @Override
    public Map<String, ShadowDatabaseConfig> getShadowDatasourceConfigs() {
        return GlobalConfig.getInstance().getShadowDatasourceConfigs();
    }

    @Override
    public boolean containsShadowDatabaseConfig(String key) {
        return GlobalConfig.getInstance().containsShadowDatabaseConfig(key);
    }

    @Override
    public ShadowDatabaseConfig getShadowDatabaseConfig(String key) {
        return GlobalConfig.getInstance().getShadowDatabaseConfig(key);
    }

    @Override
    public boolean isEmptyShadowDatabaseConfigs() {
        return GlobalConfig.getInstance().isEmptyShadowDatabaseConfigs();
    }

    @Override
    public Map<String, ShadowRedisConfig> getShadowRedisConfigs() {
        return GlobalConfig.getInstance().getShadowRedisConfigs();
    }

    @Override
    public ShadowRedisConfig getShadowRedisConfig(String key) {
        return GlobalConfig.getInstance().getShadowRedisConfig(key);
    }

    @Override
    public Set<String> getTraceRules() {
        return GlobalConfig.getInstance().getTraceRules();
    }

    @Override
    public Set<String> getSearchWhiteList() {
        return GlobalConfig.getInstance().getSearchWhiteList();
    }

    @Override
    public Set<String> getCacheKeyWhiteList() {
        return GlobalConfig.getInstance().getCacheKeyWhiteList();
    }

    @Override
    public Set<String> getMqWhiteList() {
        return GlobalConfig.getInstance().getMqWhiteList();
    }

    @Override
    public Set<String> getUrlWhiteList() {
        return GlobalConfig.getInstance().getUrlWhiteList();
    }

    @Override
    public Set<String> getDubboNameWhiteList() {
        return GlobalConfig.getInstance().getRpcNameWhiteList();
    }
}
