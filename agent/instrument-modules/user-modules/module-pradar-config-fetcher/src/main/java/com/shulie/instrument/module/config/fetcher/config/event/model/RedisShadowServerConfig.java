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

import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowRedisServerDisableEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Auther: vernon
 * @Date: 2020/11/27 15:57
 * @Description:
 */
public class RedisShadowServerConfig implements IChange<Map<String
        , ShadowRedisConfig>, ApplicationConfig> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisShadowServerConfig() {
    }

    private static RedisShadowServerConfig instance = new RedisShadowServerConfig();

    public static RedisShadowServerConfig getInstance() {
        return instance;
    }


    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig applicationConfig,
                                         Map<String, ShadowRedisConfig> newConfig) {

        Map<String, ShadowRedisConfig> old = GlobalConfig.getInstance().getShadowRedisConfigs();
        if (old.size() == 0 && newConfig.size() == 0) {
            return Boolean.FALSE;
        } else if (old.size() == 0 && newConfig.size() != 0) {
            GlobalConfig.getInstance().setShadowRedisConfigs(newConfig);
            applicationConfig.setShadowRedisConfigs(newConfig);
            return Boolean.TRUE;
        } else if (old.size() != 0 && newConfig.size() == 0) {
            GlobalConfig.getInstance().setShadowRedisConfigs(newConfig);
            applicationConfig.setShadowRedisConfigs(newConfig);
            for (Map.Entry<String, ShadowRedisConfig> entry : old.entrySet()) {
                ShadowRedisConfig oldValue = entry.getValue();
                ShadowRedisServerDisableEvent event = new ShadowRedisServerDisableEvent(Collections.singletonList(oldValue));
                EventRouter.router().publish(event);
            }
            return Boolean.TRUE;
        } else if (old.size() != 0 && newConfig.size() != 0) {
            Boolean change = false;
            if (old.size() != newConfig.size()) {
                change = true;
            }

            List<ShadowRedisConfig> removeKeys = new ArrayList<ShadowRedisConfig>();
            for (Map.Entry entry : old.entrySet()) {

                String oldKey = String.valueOf(entry.getKey());
                ShadowRedisConfig oldValue = (ShadowRedisConfig) entry.getValue();
                if (!newConfig.containsKey(oldKey)) {
                    change = true;
                    removeKeys.add(oldValue);
                } else if (newConfig.containsKey(oldKey)) {
                    if (!oldValue.equals(newConfig.get(oldKey))) {
                        removeKeys.add(oldValue);
                        change = true;
                    }
                }
            }

            if (removeKeys.size() > 0) {
                EventRouter.router().publish(new ShadowRedisServerDisableEvent(removeKeys));
            }
            if (change) {
                GlobalConfig.getInstance().setShadowRedisConfigs(newConfig);
                applicationConfig.setShadowRedisConfigs(newConfig);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;


        }
        return Boolean.FALSE;
    }
}
