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
package com.shulie.instrument.simulator.api;

import com.shulie.instrument.simulator.api.instrument.EnhanceTemplate;
import com.shulie.instrument.simulator.api.resource.ModuleController;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.message.Action;
import com.shulie.instrument.simulator.message.Messager;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 模块生命周期适配器，用于简化接口实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class ModuleLifecycleAdapter implements ModuleLifecycle {
    @Resource
    protected SimulatorConfig simulatorConfig;
    @Resource
    protected ModuleEventWatcher moduleEventWatcher;
    @Resource
    protected EnhanceTemplate enhanceTemplate;
    @Resource
    protected ModuleController moduleController;

    protected Map<String, Action> actions = new HashMap<String, Action>();

    protected String moduleName;

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void addReleaseResource(ReleaseResource releaseResource) {
        this.moduleController.addReleaseResource(releaseResource);
    }

    protected void registerAction(String actionName, Action action) {
        this.actions.put(actionName, action);
    }

    @Override
    public void onLoad() throws Throwable {
    }

    @Override
    public void onUnload() throws Throwable {

    }

    @Override
    public void onActive() throws Throwable {

    }

    @Override
    public void onFrozen() throws Throwable {
    }

    @Override
    public void loadCompleted() {
        if (!actions.isEmpty()) {
            Messager.registerAction(moduleName, new Action() {
                @Override
                public Object onAction(String action, Object... args) {
                    if (!actions.containsKey(action)) {
                        return Action.ACTION_NOT_FOUND;
                    }
                    Action action1 = actions.get(action);
                    return action1.onAction(action, args);
                }
            });
        }
    }

}
