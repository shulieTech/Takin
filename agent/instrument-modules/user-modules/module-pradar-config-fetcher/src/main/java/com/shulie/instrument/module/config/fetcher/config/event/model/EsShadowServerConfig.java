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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pamirs.pradar.internal.config.ShadowEsServerConfig;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowEsServerDisableEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowEsServerRegisterEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;

/**
 * @Auther: vernon
 * @Date: 2020/11/27 15:57
 * @Description:
 */
public class EsShadowServerConfig implements IChange<Map<String, ShadowEsServerConfig>, ApplicationConfig> {

    public EsShadowServerConfig() {
    }

    private static final EsShadowServerConfig instance = new EsShadowServerConfig();

    public static EsShadowServerConfig getInstance() {
        return instance;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig applicationConfig,
        Map<String, ShadowEsServerConfig> newConfig) {
        Map<String, ShadowEsServerConfig> old = applicationConfig.getShadowEsServerConfigs();
        List<ShadowEsServerConfig> removes = getRemoves(newConfig, old);
        List<ShadowEsServerConfig> adds = getAdds(newConfig, old);
        boolean result = removes.size() > 0 || adds.size() > 0;
        if (result) {
            applicationConfig.setShadowEsServerConfigs(newConfig);
            GlobalConfig.getInstance().setShadowEsServerConfigs(newConfig);
            publishRegisterEvents(adds);
            publishDisableEvents(removes);
        }
        return result;
    }

    private void publishDisableEvents(List<ShadowEsServerConfig> configs) {
        if (configs.size() > 0) {
            EventRouter.router().publish(new ShadowEsServerDisableEvent(configs));
        }
    }

    private void publishRegisterEvents(List<ShadowEsServerConfig> configs) {
        if (configs.size() > 0) {
            EventRouter.router().publish(new ShadowEsServerRegisterEvent(configs));
        }
    }

    private List<ShadowEsServerConfig> getRemoves(Map<String, ShadowEsServerConfig> newConfig,
        Map<String, ShadowEsServerConfig> old) {
        return leftNotInRight(old, newConfig);
    }

    private List<ShadowEsServerConfig> getAdds(Map<String, ShadowEsServerConfig> newConfig,
        Map<String, ShadowEsServerConfig> old) {
        return leftNotInRight(newConfig, old);
    }

    private List<ShadowEsServerConfig> leftNotInRight(Map<String, ShadowEsServerConfig> left,
        Map<String, ShadowEsServerConfig> right) {
        List<ShadowEsServerConfig> result = new ArrayList<ShadowEsServerConfig>();
        for (Entry<String, ShadowEsServerConfig> entry : left.entrySet()) {
            if (!right.containsKey(entry.getKey())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }
}
