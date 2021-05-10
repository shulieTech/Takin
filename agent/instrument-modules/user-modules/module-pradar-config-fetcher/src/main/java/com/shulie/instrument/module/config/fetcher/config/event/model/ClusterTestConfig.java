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
package com.shulie.instrument.module.config.fetcher.config.event.model;

import com.pamirs.pradar.internal.config.MockConfig;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/27 12:35 上午
 */
public class ClusterTestConfig implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 中间件类型
     *
     * @see com.pamirs.pradar.MiddlewareType
     */
    private int middlewareType;

    /**
     * 对应的影子配置, 数据库的影子库, mq 的影子 server, cache 的影子 server,
     * 搜索的影子 server，job 的影子 job
     */
    private Map<String, Map<String, String>> shadowDatabaseConfigs;

    /**
     * 白名单配置
     */
    private Set<String> whiteList;

    /**
     * 黑名单配置
     */
    private Set<String> blackList;

    /**
     * 挡板配置
     */
    private Map<String, MockConfig> mockConfigs;

    public int getMiddlewareType() {
        return middlewareType;
    }

    public void setMiddlewareType(int middlewareType) {
        this.middlewareType = middlewareType;
    }

    public Map<String, Map<String, String>> getShadowDatabaseConfigs() {
        return shadowDatabaseConfigs;
    }

    public void setShadowDatabaseConfigs(Map<String, Map<String, String>> shadowDatabaseConfigs) {
        this.shadowDatabaseConfigs = shadowDatabaseConfigs;
    }

    public Set<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(Set<String> whiteList) {
        this.whiteList = whiteList;
    }

    public Set<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(Set<String> blackList) {
        this.blackList = blackList;
    }

    public Map<String, MockConfig> getMockConfigs() {
        return mockConfigs;
    }

    public void setMockConfigs(Map<String, MockConfig> mockConfigs) {
        this.mockConfigs = mockConfigs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusterTestConfig that = (ClusterTestConfig) o;

        if (middlewareType != that.middlewareType) return false;
        if (shadowDatabaseConfigs != null ? !shadowDatabaseConfigs.equals(that.shadowDatabaseConfigs) : that.shadowDatabaseConfigs != null)
            return false;
        if (whiteList != null ? !whiteList.equals(that.whiteList) : that.whiteList != null) return false;
        if (blackList != null ? !blackList.equals(that.blackList) : that.blackList != null) return false;
        return mockConfigs != null ? mockConfigs.equals(that.mockConfigs) : that.mockConfigs == null;
    }

    @Override
    public int hashCode() {
        int result = middlewareType;
        result = 31 * result + (shadowDatabaseConfigs != null ? shadowDatabaseConfigs.hashCode() : 0);
        result = 31 * result + (whiteList != null ? whiteList.hashCode() : 0);
        result = 31 * result + (blackList != null ? blackList.hashCode() : 0);
        result = 31 * result + (mockConfigs != null ? mockConfigs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClusterTestConfig{" +
                "middlewareType=" + middlewareType +
                ", shadowDatabaseConfigs=" + shadowDatabaseConfigs +
                ", whiteList=" + whiteList +
                ", blackList=" + blackList +
                ", mockConfigs=" + mockConfigs +
                '}';
    }
}
