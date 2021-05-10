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
package com.shulie.instrument.module.config.fetcher.config.impl;


import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;
import com.shulie.instrument.module.config.fetcher.config.resolver.ConfigResolver;

/**
 * 压测总配置项
 *
 * @author shiyajian
 * @since 2020-08-11
 */
public class ClusterTestConfig extends AbstractConfig<ClusterTestConfig> {
    public static Boolean getApplicationSwitcher = Boolean.FALSE;
    public static Boolean getWhiteListSwitcher = Boolean.FALSE;

    /**
     * 全局压测总开关
     */
    private boolean globalSwitchOn;

    /**
     * 白名单总开关
     */
    private boolean whiteListSwitchOn;

    public ClusterTestConfig(ConfigResolver resolver) {
        super(resolver);
        this.globalSwitchOn = false;
        this.whiteListSwitchOn = false;
        this.type = ClusterTestConfig.class;
    }

    @Override
    public void trigger(FIELDS... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }
        ClusterTestConfig clusterTestConfig = (ClusterTestConfig) resolver.fetch(fields);
        if (clusterTestConfig != null) {
            refreshFields(clusterTestConfig, fields);
        }
    }

    public boolean isGlobalSwitchOn() {
        return globalSwitchOn;
    }

    public void setGlobalSwitchOn(boolean globalSwitchOn) {
        this.globalSwitchOn = globalSwitchOn;
    }

    public boolean isWhiteListSwitchOn() {
        return whiteListSwitchOn;
    }

    public void setWhiteListSwitchOn(boolean whiteListSwitchOn) {
        this.whiteListSwitchOn = whiteListSwitchOn;
    }

    public void refreshFields(ClusterTestConfig clusterTestConfig, FIELDS... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }
        for (FIELDS field : fields) {
            switch (field) {
                case GLOBAL_SWITCHON:
                    change(FIELDS.GLOBAL_SWITCHON, clusterTestConfig.isGlobalSwitchOn());
                    break;
                case WHITE_LIST_SWITCHON:
                    change(FIELDS.WHITE_LIST_SWITCHON, clusterTestConfig.isWhiteListSwitchOn());
                    break;
            }
        }
    }

    @Override
    public void refresh(ClusterTestConfig clusterTestConfig) {
        if (getApplicationSwitcher) {
            change(FIELDS.GLOBAL_SWITCHON, clusterTestConfig.isGlobalSwitchOn());
        }
        if (getWhiteListSwitcher) {
            change(FIELDS.WHITE_LIST_SWITCHON, clusterTestConfig.isWhiteListSwitchOn());
        }
    }
}
