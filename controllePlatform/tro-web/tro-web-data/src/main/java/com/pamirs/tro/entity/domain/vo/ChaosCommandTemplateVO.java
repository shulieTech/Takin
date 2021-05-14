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
 * @ClassName: ChaosCommandTemplateVO
 * @package: com.pamirs.tro.entity.domain.vo
 * @Date: 2019/5/8 0008 14:41
 * @Description: 混沌工程命令模板VO
 */
public class ChaosCommandTemplateVO implements Serializable {

    private Long id;

    private String name;

    private String keyword;

    private String commandTemplate;

    private Byte isSystem;

    private String paramDesc;

    private Integer pluginCount;

    private Integer faultType;

    private Long[] pluginKeys;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCommandTemplate() {
        return commandTemplate;
    }

    public void setCommandTemplate(String commandTemplate) {
        this.commandTemplate = commandTemplate;
    }

    public Byte getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Byte isSystem) {
        this.isSystem = isSystem;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public Integer getPluginCount() {
        return pluginCount;
    }

    public void setPluginCount(Integer pluginCount) {
        this.pluginCount = pluginCount;
    }

    public Long[] getPluginKeys() {
        return pluginKeys;
    }

    public void setPluginKeys(Long[] pluginKeys) {
        this.pluginKeys = pluginKeys;
    }

    public Integer getFaultType() {
        return faultType;
    }

    public ChaosCommandTemplateVO setFaultType(Integer faultType) {
        this.faultType = faultType;
        return this;
    }
}
