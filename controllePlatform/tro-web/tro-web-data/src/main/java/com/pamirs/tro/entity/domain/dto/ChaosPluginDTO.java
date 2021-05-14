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

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: 710524
 * @ClassName: ChaosPluginDTO
 * @package: com.pamirs.tro.entity.domain.dto
 * @Description: 插件信息
 */

public class ChaosPluginDTO implements Serializable {

    private Long id;

    private Long appId;

    private Integer pluginType;

    private String pluginName;

    private String status;

    private String appName;

    private String pluginPackageName;

    private String pluginPackageVersion;

    private String pluginFile;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date createTime;

    private String note;

    public Integer getPluginType() {
        return pluginType;
    }

    public void setPluginType(Integer pluginType) {
        this.pluginType = pluginType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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
        this.status = status;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPluginPackageName() {
        return pluginPackageName;
    }

    public void setPluginPackageName(String pluginPackageName) {
        this.pluginPackageName = pluginPackageName;
    }

    public String getPluginPackageVersion() {
        return pluginPackageVersion;
    }

    public void setPluginPackageVersion(String pluginPackageVersion) {
        this.pluginPackageVersion = pluginPackageVersion;
    }

    public String getPluginFile() {
        return pluginFile;
    }

    public void setPluginFile(String pluginFile) {
        this.pluginFile = pluginFile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
