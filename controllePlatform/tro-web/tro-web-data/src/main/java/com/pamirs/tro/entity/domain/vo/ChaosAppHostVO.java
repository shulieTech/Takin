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

package com.pamirs.tro.entity.domain.vo;

import java.io.Serializable;

/**
 * @Author: 710524
 * @ClassName: ChaosAppHostVO
 * @Package: com.monitor.platform.bean.vo
 * @Date: 2019/5/5 0005 14:18
 * @Description: 应用节点
 */

public class ChaosAppHostVO implements Serializable {

    private Long id;

    private String name;

    private String ip;

    private Long appCode;

    private String appName;

    private String port;

    public ChaosAppHostVO() {
    }

    public ChaosAppHostVO(Long appCode, String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.appCode = appCode;
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getAppCode() {
        return appCode;
    }

    public void setAppCode(Long appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
