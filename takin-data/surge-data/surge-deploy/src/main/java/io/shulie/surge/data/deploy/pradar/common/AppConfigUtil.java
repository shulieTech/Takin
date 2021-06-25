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

package io.shulie.surge.data.deploy.pradar.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.zk.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Singleton
public class AppConfigUtil {

    @Inject
    @Named("config.simpling.app.zk.path")
    private String appSamplingZkPath;

    @Inject
    @Named("config.simpling.global.zk.path")
    private String globalSamplingPath;

    @Inject
    @Named("config.slowSql.app.zk.path")
    private String appSlowSqlZkPath;

    @Inject
    @Named("config.slowSql.global.zk.path")
    private String globalSlowSqlPath;

    @Inject
    private ZkClient zkClient;

    /**
     * 采样率缓存
     */
    LoadingCache<String, Integer> samplingCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
        @Override
        public Integer load(String appName) throws Exception {
            // 应用自定义采样率
            String zkPath = appSamplingZkPath.replace("{appName}", appName);
            String simpling = String.valueOf(1);
            if (zkClient.exists(zkPath)) {
                if (zkClient.exists(zkPath)) {
                    byte[] data = zkClient.getData(zkPath);
                    if (data != null) {
                        simpling = new String(data, "utf-8");
                    }
                }
            }
            // 全局采样率
            if (StringUtils.isBlank(simpling)) {
                if (zkClient.exists(globalSamplingPath)) {
                    byte[] data = zkClient.getData(globalSamplingPath);
                    if (data != null) {
                        simpling = new String(data, "utf-8");
                    }
                }
            }
            return NumberUtils.toInt(simpling, 1);
        }
    });

    /**
     * 慢SQL配置缓存
     */
    LoadingCache<String, Integer> slowSqlCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
        @Override
        public Integer load(String appName) throws Exception {
            // 应用自定义采样率
            String zkPath = appSlowSqlZkPath.replace("{appName}", appName);
            String simpling = null;
            if (zkClient.exists(zkPath)) {
                byte[] data = zkClient.getData(zkPath);
                if (data != null) {
                    simpling = new String(data, "utf-8");
                }
            }
            // 全局采样率
            if (StringUtils.isBlank(simpling)) {
                if (zkClient.exists(globalSlowSqlPath)) {
                    byte[] data = zkClient.getData(globalSlowSqlPath);
                    if (data != null) {
                        simpling = new String(data, "utf-8");
                    }
                }
            }
            return NumberUtils.toInt(simpling, 1000);
        }
    });

    /**
     * 获取应用采样率配置
     *
     * @param appName
     * @return
     */
    public int getAppSamplingByAppName(String appName) {
        try {
            return samplingCache.get(appName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取应用慢SQL配置
     *
     * @param appName
     * @return
     */
    public int getAppSlowSqlConfigByAppName(String appName) {
        try {
            return slowSqlCache.get(appName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 1000;
    }
}
