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

import com.pamirs.pradar.ConfigNames;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.pressurement.agent.shared.exit.ArbiterHttpExit;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;

import java.util.Set;

/**
 * @author: wangjian
 * @since : 2020/9/8 17:02
 */
public class UrlWhiteList implements IChange<Set<String>, ApplicationConfig> {

    private static final UrlWhiteList INSTANCE = new UrlWhiteList();

    private UrlWhiteList() {
    }

    public static UrlWhiteList getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig applicationConfig, Set<String> newValue) {
        Set<String> urlWhiteList = GlobalConfig.getInstance().getUrlWhiteList();
        if (ObjectUtils.equals(urlWhiteList.size(), newValue.size())
                && CollectionUtils.equals(urlWhiteList, newValue)) {
            return Boolean.FALSE;
        }
        // 变更后配置更新到内存
        applicationConfig.setUrlWhiteList(newValue);
        GlobalConfig.getInstance().setUrlWhiteList(newValue);
        ArbiterHttpExit.clearHttpMatch();
        PradarSwitcher.turnConfigSwitcherOn(ConfigNames.URL_WHITE_LIST);
        return Boolean.TRUE;
    }
}
