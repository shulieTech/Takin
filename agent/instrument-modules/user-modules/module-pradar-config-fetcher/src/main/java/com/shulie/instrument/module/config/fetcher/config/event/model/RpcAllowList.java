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
 * @since : 2020/9/8 17:14
 */
public class RpcAllowList implements IChange<Set<String>, ApplicationConfig> {

    private static final RpcAllowList INSTANCE = new RpcAllowList();

    private RpcAllowList() {
    }

    public static RpcAllowList getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean compareIsChangeAndSet(ApplicationConfig config, Set<String> newValue) {
        Set<String> rpcNameList = GlobalConfig.getInstance().getRpcNameWhiteList();
        if (ObjectUtils.equals(rpcNameList.size(), newValue.size()) && CollectionUtils.equals(rpcNameList, newValue)) {
            return Boolean.FALSE;
        }
        config.setRpcNameWhiteList(newValue);
        GlobalConfig.getInstance().setRpcNameWhiteList(newValue);
        PradarSwitcher.turnConfigSwitcherOn(ConfigNames.DUBBO_ALLOW_LIST);
        ArbiterHttpExit.clearRpcMatch();
        // 变更后配置更新到内存
        return Boolean.TRUE;
    }
}
