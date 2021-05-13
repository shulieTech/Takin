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

package io.shulie.tro.k8s.model;

/**
 * 应用信息
 *
 * @author lipeng
 * @date 2021-02-25 2:27 下午
 */
public class ApplicationInfo {

    //应用名称
    private String appName;

    //实例数量 默认1个
    private Integer replicas = 1;

    //cpu 默认2核
    private Integer cpu = 2000;

    //内存 默认4G
    private Integer memory = 4096;

    //配置项ID
    private Long configMapId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Long getConfigMapId() {
        return configMapId;
    }

    public void setConfigMapId(Long configMapId) {
        this.configMapId = configMapId;
    }
}
