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

package com.pamirs.tro.entity.domain.dto;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.TIsolationAppMainConfig;

public class TIsolationAppConfigDTO extends TIsolationAppMainConfig {

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 注册中心
     */
    private Map<Long, String> dubboRegistries;

    /**
     * eureka服务
     */
    private Map<Long, String> eurekaServers;

    /**
     * RocketMQ集群
     */
    private Map<Long, String> rocketMQClusters;

    /**
     * 应用节点
     */
    private List<String> appNodes;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Map<Long, String> getDubboRegistries() {
        return dubboRegistries;
    }

    public void setDubboRegistries(Map<Long, String> dubboRegistries) {
        this.dubboRegistries = dubboRegistries;
    }

    public Map<Long, String> getEurekaServers() {
        return eurekaServers;
    }

    public void setEurekaServers(Map<Long, String> eurekaServers) {
        this.eurekaServers = eurekaServers;
    }

    public Map<Long, String> getRocketMQClusters() {
        return rocketMQClusters;
    }

    public void setRocketMQClusters(Map<Long, String> rocketMQClusters) {
        this.rocketMQClusters = rocketMQClusters;
    }

    public List<String> getAppNodes() {
        return appNodes;
    }

    public void setAppNodes(List<String> appNodes) {
        this.appNodes = appNodes;
    }

}
