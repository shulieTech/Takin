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

package com.pamirs.tro.entity.domain.query;

import com.pamirs.tro.common.constant.Constants;

/**
 * @Author: 710524
 * @ClassName: ChaosPluginQuery
 * @package: com.pamirs.tro.entity.domain.query
 * @Description: 插件查询
 */
public class ChaosPluginQuery extends QueryPage {

    private Long appId;

    private Integer pluginType;
    private String pluginName;
    private String pluginPackageVersion;
    private String status;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        if (appId != null && appId > 0) {
            this.appId = appId;
        }
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (Constants.Y.equalsIgnoreCase(status) || Constants.N.equalsIgnoreCase(status)) {
            this.status = status.toUpperCase();
        }
    }

    public Integer getPluginType() {
        return pluginType;
    }

    public void setPluginType(Integer pluginType) {
        if (pluginType != null && (Constants.PLUGIN_TYPE_MONITOR == pluginType
            || Constants.PLUGIN_TYPE_DETECT == pluginType)) {
            this.pluginType = pluginType;
        }
    }

    public String getPluginPackageVersion() {
        return pluginPackageVersion;
    }

    public void setPluginPackageVersion(String pluginPackageVersion) {
        this.pluginPackageVersion = pluginPackageVersion;
    }
}
