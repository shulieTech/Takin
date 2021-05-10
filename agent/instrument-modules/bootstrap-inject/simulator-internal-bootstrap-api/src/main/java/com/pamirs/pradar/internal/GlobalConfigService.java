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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/8 8:53 下午
 */
public class GlobalConfigService {
    private static IGlobalConfigService instance;

    public static void registerService(IGlobalConfigService service) {
        instance = service;
    }

    /**
     * 获取所有的对外开放的 API 列表 (springmvc 暴露的)
     *
     * @return
     */
    public static Set<String> getApis() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getApis();
    }

    /**
     * redis 是否是影子库模式
     *
     * @return
     */
    public static boolean isShadowDbRedisServer() {
        if (instance == null) {
            return false;
        }
        return instance.isShadowDbRedisServer();
    }

    /**
     * 获取所有的mock 配置
     *
     * @return
     */
    public static Set<MockConfig> getMockConfigs() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getMockConfigs();
    }

    /**
     * 获取所有的错误 sql 列表
     *
     * @return
     */
    public static Set<String> getWrongSqlDetail() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getWrongSqlDetail();
    }

    /**
     * 添加执行错误的 sql
     *
     * @param sql
     */
    public static void addWrongSqlDetail(String sql) {
        if (instance == null) {
            return;
        }
        instance.addWrongSqlDetail(sql);
    }

    /**
     * 获取所有的影子表配置
     * <p>
     * key -> url|username
     *
     * @return
     */
    public static Map<String, Set<String>> getShadowTables() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getShadowTables();
    }

    /**
     * 获取所有注册错误的 job
     *
     * @return
     */
    public static Set<ShadowJob> getErrorRegisterJobs() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getErrorRegisterJobs();
    }

    /**
     * 添加注册错误的 job
     *
     * @param shadowJob
     */
    public static void addErrorRegisteredJob(ShadowJob shadowJob) {
        if (instance == null) {
            return;
        }
        instance.addErrorRegisteredJob(shadowJob);
    }

    /**
     * 清除所有注册出错的 job
     */
    public static void clearErrorRegisteredJobs() {
        if (instance == null) {
            return;
        }
        instance.clearErrorRegisteredJobs();
    }

    /**
     * 获取所有已经注册的 job
     *
     * @return
     */
    public static Map<String, ShadowJob> getRegisteredJobs() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getRegisteredJobs();
    }

    /**
     * 添加注册成功的 job
     *
     * @param shadowJob
     */
    public static void addRegisteredJob(ShadowJob shadowJob) {
        if (instance == null) {
            return;
        }
        instance.addRegisteredJob(shadowJob);
    }

    /**
     * 获取所有需要注册的 job 列表
     *
     * @return
     */
    public static Map<String, ShadowJob> getNeedRegisterJobs() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getNeedRegisterJobs();
    }

    /**
     * 获取所有需要停止的 job 列表
     *
     * @return
     */
    public static Map<String, ShadowJob> getNeedStopJobs() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getNeedStopJobs();
    }

    /**
     * 添加需要停止的 job 列表
     *
     * @param shadowJob
     */
    public static void addNeedStopJobs(ShadowJob shadowJob) {
        if (instance == null) {
            return;
        }
        instance.addNeedStopJobs(shadowJob);
    }

    /**
     * 移除注册的 job
     *
     * @param shadowJob
     */
    public static void removeRegisteredJob(ShadowJob shadowJob) {
        if (instance == null) {
            return;
        }
        instance.removeRegisteredJob(shadowJob);
    }

    /**
     * 获取所有的 job 适配器
     *
     * @return
     */
    public static Map<String, JobAdapter> getJobAdaptors() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getJobAdaptors();
    }

    /**
     * 添加 job 适配器
     *
     * @param name
     * @param jobAdapter
     */
    public static void addJobAdaptor(String name, JobAdapter jobAdapter) {
        if (instance == null) {
            return;
        }
        instance.addJobAdaptor(name, jobAdapter);
    }

    /**
     * 根据 job 获取 job 适配器
     *
     * @param name
     * @return
     */
    public static JobAdapter getJobAdaptor(String name) {
        if (instance == null) {
            return null;
        }
        return instance.getJobAdaptor(name);
    }

    /**
     * 获取影子数据源配置
     * key -> url|username
     *
     * @return
     */
    public static Map<String, ShadowDatabaseConfig> getShadowDatasourceConfigs() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getShadowDatasourceConfigs();
    }

    /**
     * 判断是否包含指定key 的影子库配置
     *
     * @param key url|username
     * @return
     */
    public static boolean containsShadowDatabaseConfig(String key) {
        if (instance == null) {
            return false;
        }
        return instance.containsShadowDatabaseConfig(key);
    }

    /**
     * 获取指定 key 的影子库配置
     *
     * @param key url|username
     * @return
     */
    public static ShadowDatabaseConfig getShadowDatabaseConfig(String key) {
        if (instance == null) {
            return null;
        }
        return instance.getShadowDatabaseConfig(key);
    }

    /**
     * 是否影子数据库配置为空
     */
    public static boolean isEmptyShadowDatabaseConfigs() {
        if (instance == null) {
            return true;
        }
        return instance.isEmptyShadowDatabaseConfigs();
    }

    /**
     * 获取所有的影子 redis 配置
     *
     * @return
     */
    public static Map<String, ShadowRedisConfig> getShadowRedisConfigs() {
        if (instance == null) {
            return Collections.EMPTY_MAP;
        }
        return instance.getShadowRedisConfigs();
    }

    /**
     * 根据 key 获取影子 redis 的配置
     *
     * @param key
     * @return
     */
    public static ShadowRedisConfig getShadowRedisConfig(String key) {
        if (instance == null) {
            return null;
        }
        return instance.getShadowRedisConfig(key);
    }

    /**
     * 获取所有的入口规则
     *
     * @return
     */
    public static Set<String> getTraceRules() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getTraceRules();
    }

    /**
     * 返回搜索白名单，在搜索白名单中则可以走业务索引，但是只有只读操作，写操作不允许
     *
     * @return
     */
    public static Set<String> getSearchWhiteList() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getSearchWhiteList();
    }

    /**
     * 获取缓存 key 白名单
     *
     * @return
     */
    public static Set<String> getCacheKeyWhiteList() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getCacheKeyWhiteList();
    }

    /**
     * 获取 mq 白名单
     *
     * @return
     */
    public static Set<String> getMqWhiteList() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getMqWhiteList();
    }

    /**
     * 获取 url 白名单
     *
     * @return
     */
    public static Set<String> getUrlWhiteList() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getUrlWhiteList();
    }

    /**
     * 获取 dubbo 的白名单
     *
     * @return
     */
    public static Set<String> getDubboNameWhiteList() {
        if (instance == null) {
            return Collections.EMPTY_SET;
        }
        return instance.getDubboNameWhiteList();
    }

    /**
     * 判断是否已经初始化完成
     *
     * @return
     */
    public static boolean isInited() {
        return instance != null;
    }
}
