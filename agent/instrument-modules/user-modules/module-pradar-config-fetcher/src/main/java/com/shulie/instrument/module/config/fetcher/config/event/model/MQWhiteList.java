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
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import org.apache.commons.lang.ObjectUtils;

import java.util.Set;

/**
 * @ClassName: RabbitWhiteList
 * @author: wangjian
 * @Date: 2020/9/29 09:54
 * @Description:
 */
public class MQWhiteList implements IChange<Set<String>, ApplicationConfig> {

    private static final MQWhiteList INSTANCE = new MQWhiteList();

    public static MQWhiteList getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig currentValue, Set<String> newValue) {
        Set<String> mqWhiteList = GlobalConfig.getInstance().getMqWhiteList();
        if (ObjectUtils.equals(mqWhiteList.size(), newValue.size())
                && mqWhiteList.containsAll(newValue)) {
            return Boolean.FALSE;
        }
        currentValue.setMqList(newValue);
        GlobalConfig.getInstance().setMqWhiteList(newValue);
        PradarSwitcher.turnConfigSwitcherOn(ConfigNames.MQ_WHITE_LIST);
        return Boolean.TRUE;
    }
}
