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
import com.pamirs.pradar.internal.config.ShadowJob;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowJobDisableEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowJobRegisterEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: wangjian
 * @since : 2020/9/8 17:36
 */
public class ShadowJobConfig implements IChange<Set<ShadowJob>, ApplicationConfig> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShadowJobConfig.class.getName());

    private static final ShadowJobConfig INSTANCE = new ShadowJobConfig();

    private ShadowJobConfig() {

    }

    public static ShadowJobConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig applicationConfig, Set<ShadowJob> newValue) {
        Set<ShadowJob> oldShadowJobs = new HashSet<ShadowJob>(GlobalConfig.getInstance().getRegisteredJobs().values());
        /**
         * 影子job不支持热更新, 即不支持如果job已经存在，不支持该job的配置更新
         */

        if (ObjectUtils.equals(oldShadowJobs.size(), newValue.size())) {
            Map<Long, ShadowJob> oldShadowJobMap = new HashMap<Long, ShadowJob>(16, 1);
            for (ShadowJob shadowJob : oldShadowJobs) {
                oldShadowJobMap.put(shadowJob.getId(), shadowJob);
            }
            Map<Long, ShadowJob> newShadowJobMap = new HashMap<Long, ShadowJob>(16, 1);
            for (ShadowJob shadowJob : newValue) {
                newShadowJobMap.put(shadowJob.getId(), shadowJob);
            }
            boolean change = false;
            for (Map.Entry<Long, ShadowJob> entry : newShadowJobMap.entrySet()) {
                if (!oldShadowJobMap.containsKey(entry.getKey()) || !oldShadowJobMap.get(entry.getKey()).toString().equals(entry.getValue().toString())) {
                    change = true;
                }
                if (change) {
                    break;
                }
            }
            if (!change) {
                return Boolean.FALSE;
            }
        }
        applicationConfig.setShadowJobs(newValue);
        Set<ShadowJob> addShadowJobs = calculateAddJobs(newValue, oldShadowJobs);
        Set<ShadowJob> removeShadowJobs = calculateRemoveJobs(newValue, oldShadowJobs);

        for (ShadowJob shadowJob : addShadowJobs) {
            ShadowJobRegisterEvent registerEvent = new ShadowJobRegisterEvent(shadowJob);
            EventRouter.router().publish(registerEvent);
        }

        for (ShadowJob shadowJob : removeShadowJobs) {
            ShadowJobDisableEvent disableEvent = new ShadowJobDisableEvent(shadowJob);
            EventRouter.router().publish(disableEvent);
        }
        PradarSwitcher.turnConfigSwitcherOn(ConfigNames.SHADOW_JOB_CONFIGS);
        if (LOGGER.isInfoEnabled() && (CollectionUtils.isNotEmpty(removeShadowJobs) || CollectionUtils.isNotEmpty(addShadowJobs))) {
            for (ShadowJob delete : removeShadowJobs) {
                LOGGER.info("need delete job：" + delete.toString());
            }
            for (ShadowJob newJob : addShadowJobs) {
                LOGGER.info("need add job：" + newJob.toString());
            }
        }

        return Boolean.TRUE;
    }

    private Set<ShadowJob> calculateAddJobs(Set<ShadowJob> newShadowJobs, Set<ShadowJob> oldShadowJobs) {
        Set<ShadowJob> shadowJobs = new HashSet<ShadowJob>();
        //只添加启动的job
        for (ShadowJob shadowJob : newShadowJobs) {
            if ("0".equals(shadowJob.getStatus())) {
                shadowJobs.add(shadowJob);
            }
        }
        shadowJobs.removeAll(oldShadowJobs);
        return shadowJobs;
    }

    private Set<ShadowJob> calculateRemoveJobs(Set<ShadowJob> newShadowJobs, Set<ShadowJob> oldShadowJobs) {
        Set<ShadowJob> shadowJobs = new HashSet<ShadowJob>();
        shadowJobs.addAll(oldShadowJobs);
        //去除禁用的job 0是启用 1是禁用，如果任务在控制台被删除，也将被停止
        for (ShadowJob shadowJob : newShadowJobs) {
            if ("0".equals(shadowJob.getStatus())) {
                shadowJobs.remove(shadowJob);
            }
        }
        return shadowJobs;
    }
}
