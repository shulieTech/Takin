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

import com.pamirs.pradar.internal.config.MockConfig;
import com.pamirs.pradar.pressurement.agent.event.impl.MockConfigAddEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.MockConfigModifyEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.MockConfigRemoveEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/24 6:19 下午
 */
public class MockConfigChanger implements IChange<Set<MockConfig>, ApplicationConfig> {

    private static final MockConfigChanger INSTANCE = new MockConfigChanger();

    private MockConfigChanger() {
    }

    public static MockConfigChanger getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig applicationConfig, Set<MockConfig> newValue) {
        Set<MockConfig> mockConfigs = GlobalConfig.getInstance().getMockConfigs();
        if (ObjectUtils.equals(mockConfigs.size(), newValue.size())
                && CollectionUtils.equals(mockConfigs, newValue)) {
            return Boolean.FALSE;
        }
        applicationConfig.setMockConfigs(newValue);

        Map<String, MockConfig> newConfigs = new HashMap<String, MockConfig>();
        Map<String, MockConfig> oldConfigs = new HashMap<String, MockConfig>();
        for (MockConfig mockConfig : newValue) {
            newConfigs.put(mockConfig.getKey(), mockConfig);
        }

        for (MockConfig mockConfig : mockConfigs) {
            oldConfigs.put(mockConfig.getKey(), mockConfig);
        }

        Set<MockConfig> addMockConfigs = calculateAddMocks(newConfigs, oldConfigs);
        Set<MockConfig> removeMockConfigs = calculateRemoveMocks(newConfigs, oldConfigs);
        Set<MockConfig> modifyMockConfigs = calculateModifyMocks(newConfigs, oldConfigs);

        if (CollectionUtils.isNotEmpty(addMockConfigs)) {
            MockConfigAddEvent addEvent = new MockConfigAddEvent(addMockConfigs);
            EventRouter.router().publish(addEvent);
        }

        if (CollectionUtils.isNotEmpty(removeMockConfigs)) {
            MockConfigRemoveEvent removeEvent = new MockConfigRemoveEvent(removeMockConfigs);
            EventRouter.router().publish(removeEvent);
        }

        if (CollectionUtils.isNotEmpty(modifyMockConfigs)) {
            MockConfigModifyEvent modifyEvent = new MockConfigModifyEvent(modifyMockConfigs);
            EventRouter.router().publish(modifyEvent);
        }
        GlobalConfig.getInstance().setMockConfigs(newValue);
        return Boolean.TRUE;
    }

    /**
     * 计算所有新增的mock配置
     *
     * @param newMockConfigs
     * @param oldMockConfigs
     * @return
     */
    private Set<MockConfig> calculateAddMocks(Map<String, MockConfig> newMockConfigs, Map<String, MockConfig> oldMockConfigs) {
        Set<String> adds = calculateAdd(newMockConfigs.keySet(), oldMockConfigs.keySet());
        Set<MockConfig> mockConfigs = new HashSet<MockConfig>();
        for (String add : adds) {
            mockConfigs.add(newMockConfigs.get(add));
        }
        return mockConfigs;
    }

    /**
     * 计算所有删除的mock配置
     *
     * @param newMockConfigs
     * @param oldMockConfigs
     * @return
     */
    private Set<MockConfig> calculateRemoveMocks(Map<String, MockConfig> newMockConfigs, Map<String, MockConfig> oldMockConfigs) {
        Set<String> adds = calculateRemove(newMockConfigs.keySet(), oldMockConfigs.keySet());
        Set<MockConfig> mockConfigs = new HashSet<MockConfig>();
        for (String add : adds) {
            mockConfigs.add(oldMockConfigs.get(add));
        }
        return mockConfigs;
    }

    /**
     * 计算所有变更的mock配置
     *
     * @param newMockConfigs
     * @param oldMockConfigs
     * @return
     */
    private Set<MockConfig> calculateModifyMocks(Map<String, MockConfig> newMockConfigs, Map<String, MockConfig> oldMockConfigs) {
        Set<String> retains = calculateRetain(newMockConfigs.keySet(), oldMockConfigs.keySet());
        Set<MockConfig> mockConfigs = new HashSet<MockConfig>();
        for (String retain : retains) {
            MockConfig newMockConfig = newMockConfigs.get(retain);
            MockConfig oldMockConfig = oldMockConfigs.get(retain);
            if (!StringUtils.equals(newMockConfig.getCodeScript(), oldMockConfig.getCodeScript())) {
                mockConfigs.add(newMockConfig);
            }

        }
        return mockConfigs;
    }

    private <T> Set<T> calculateAdd(Set<T> newSet, Set<T> oldSet) {
        Set<T> sets = new HashSet<T>();
        sets.addAll(newSet);
        sets.removeAll(oldSet);
        return sets;
    }

    private <T> Set<T> calculateRemove(Set<T> newSet, Set<T> oldSet) {
        Set<T> sets = new HashSet<T>();
        sets.addAll(oldSet);
        sets.removeAll(newSet);
        return sets;
    }

    private <T> Set<T> calculateRetain(Set<T> newSet, Set<T> oldSet) {
        Set<T> sets = new HashSet<T>();
        sets.addAll(oldSet);
        sets.retainAll(newSet);
        return sets;
    }
}
