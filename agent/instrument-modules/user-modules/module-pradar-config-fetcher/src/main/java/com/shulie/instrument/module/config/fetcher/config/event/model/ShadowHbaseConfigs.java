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

import com.pamirs.pradar.internal.config.ShadowHbaseConfig;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowHbaseDisableEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowHbaseDynamicEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;

import java.util.Map;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.shulie.instrument.module.config.fetcher.config.event.model
 * @Date 2021/4/28 10:42 上午
 */
public class ShadowHbaseConfigs implements IChange<Map<String, ShadowHbaseConfig>, ApplicationConfig>{

    private static ShadowHbaseConfigs INSTANCE = new ShadowHbaseConfigs();


    public static ShadowHbaseConfigs getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig currentValue, Map<String, ShadowHbaseConfig> newValue) {
        Map<String, ShadowHbaseConfig> old = GlobalConfig.getInstance().getShadowHbaseServerConfigs();
        boolean change = false;

        if (newValue.size() < 1) {
            GlobalConfig.getInstance().setShadowHbaseServer(false);
            for (Map.Entry<String, ShadowHbaseConfig> entry : old.entrySet()) {
                EventRouter.router().publish(new ShadowHbaseDisableEvent(entry.getKey()));
            }
            GlobalConfig.getInstance().getShadowHbaseServerConfigs().clear();
            return true;
        }

        for (Map.Entry<String, ShadowHbaseConfig> entry : old.entrySet()) {
            String key = entry.getKey();
            if (!newValue.containsKey(key)) {
                EventRouter.router().publish(new ShadowHbaseDisableEvent(key));
                change = true;
            } else {
                if (!newValue.get(key).equals(entry.getValue())) {
                    EventRouter.router().publish(new ShadowHbaseDynamicEvent(entry.getValue()));
                    change = true;
                }
            }
        }

        for (Map.Entry<String, ShadowHbaseConfig> entry : newValue.entrySet()) {
            String key = entry.getKey();
            if (old.containsKey(key)) {
                if (!old.get(key).equals(entry.getValue())) {
                    EventRouter.router().publish(new ShadowHbaseDynamicEvent(entry.getValue()));
                    change = true;
                }
            } else {
                change = true;
                EventRouter.router().publish(new ShadowHbaseDynamicEvent(entry.getValue()));
            }
        }
        GlobalConfig.getInstance().getShadowHbaseServerConfigs().clear();
        GlobalConfig.getInstance().setShadowHbaseServerConfigs(newValue);
        return change;
    }
}

