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
package com.pamirs.pradar.internal;

import com.pamirs.pradar.internal.adapter.JobAdapter;
import com.pamirs.pradar.internal.config.MockConfig;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.internal.config.ShadowJob;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;

import java.util.Map;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/8 8:53 下午
 */
public interface IGlobalConfigService {
    /**
     * 获取所有的对外开放的 API 列表 (springmvc 暴露的)
     *
     * @return
     */
    Set<String> getApis();

    /**
     * redis 是否是影子库模式
     *
     * @return
     */
    boolean isShadowDbRedisServer();

    /**
     * 获取所有的mock 配置
     *
     * @return
     */
    Set<MockConfig> getMockConfigs();

    /**
     * 获取所有的错误 sql 列表
     *
     * @return
     */
    Set<String> getWrongSqlDetail();

    /**
     * 添加执行错误的 sql
     *
     * @param sql
     */
    void addWrongSqlDetail(String sql);

    /**
     * 获取所有的影子表配置
     * <p>
     * key -> url|username
     *
     * @return
     */
    Map<String, Set<String>> getShadowTables();

    /**
     * 获取所有注册错误的 job
     *
     * @return
     */
    Set<ShadowJob> getErrorRegisterJobs();

    /**
     * 添加注册错误的 job
     *
     * @param shadowJob
     */
    void addErrorRegisteredJob(ShadowJob shadowJob);

    /**
     * 清除所有注册出错的 job
     */
    void clearErrorRegisteredJobs();

    /**
     * 获取所有已经注册的 job
     *
     * @return
     */
    Map<String, ShadowJob> getRegisteredJobs();

    /**
     * 添加注册成功的 job
     *
     * @param shadowJob
     */
    void addRegisteredJob(ShadowJob shadowJob);

    /**
     * 获取所有需要注册的 job 列表
     *
     * @return
     */
    Map<String, ShadowJob> getNeedRegisterJobs();

    /**
     * 获取所有需要停止的 job 列表
     *
     * @return
     */
    Map<String, ShadowJob> getNeedStopJobs();

    /**
     * 添加需要停止的 job 列表
     *
     * @param shadowJob
     */
    void addNeedStopJobs(ShadowJob shadowJob);

    /**
     * 移除注册的 job
     *
     * @param shadowJob
     */
    void removeRegisteredJob(ShadowJob shadowJob);

    /**
     * 获取所有的 job 适配器
     *
     * @return
     */
    Map<String, JobAdapter> getJobAdaptors();

    /**
     * 添加 job 适配器
     *
     * @param name
     * @param jobAdapter
     */
    void addJobAdaptor(String name, JobAdapter jobAdapter);

    /**
     * 根据 job 获取 job 适配器
     *
     * @param name
     * @return
     */
    JobAdapter getJobAdaptor(String name);

    /**
     * 获取影子数据源配置
     * key -> url|username
     *
     * @return
     */
    Map<String, ShadowDatabaseConfig> getShadowDatasourceConfigs();

    /**
     * 判断是否包含指定key 的影子库配置
     *
     * @param key url|username
     * @return
     */
    boolean containsShadowDatabaseConfig(String key);

    /**
     * 获取指定 key 的影子库配置
     *
     * @param key url|username
     * @return
     */
    ShadowDatabaseConfig getShadowDatabaseConfig(String key);

    /**
     * 是否影子数据库配置为空
     */
    boolean isEmptyShadowDatabaseConfigs();

    /**
     * 获取所有的影子 redis 配置
     *
     * @return
     */
    Map<String, ShadowRedisConfig> getShadowRedisConfigs();

    /**
     * 根据 key 获取影子 redis 的配置
     *
     * @param key
     * @return
     */
    ShadowRedisConfig getShadowRedisConfig(String key);

    /**
     * 获取所有的入口规则
     *
     * @return
     */
    Set<String> getTraceRules();

    /**
     * 返回搜索白名单，在搜索白名单中则可以走业务索引，但是只有只读操作，写操作不允许
     *
     * @return
     */
    Set<String> getSearchWhiteList();

    /**
     * 获取缓存 key 白名单
     *
     * @return
     */
    Set<String> getCacheKeyWhiteList();

    /**
     * 获取 mq 白名单
     *
     * @return
     */
    Set<String> getMqWhiteList();

    /**
     * 获取 url 白名单
     *
     * @return
     */
    Set<String> getUrlWhiteList();

    /**
     * 获取 dubbo 的白名单
     *
     * @return
     */
    Set<String> getDubboNameWhiteList();
}
