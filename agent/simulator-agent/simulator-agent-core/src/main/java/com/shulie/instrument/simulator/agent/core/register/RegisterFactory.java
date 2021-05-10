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
package com.shulie.instrument.simulator.agent.core.register;


import com.shulie.instrument.simulator.agent.core.register.impl.ZookeeperRegister;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注册工厂
 *
 * @author xiaobin.zfb
 * @since 2020/11/20 10:25 上午
 */
public class RegisterFactory {

    private static Map<String, Register> registers = new HashMap<String, Register>();
    private static AtomicBoolean isInited = new AtomicBoolean(false);

    public static void init(AgentConfig agentConfig) {
        if (!isInited.compareAndSet(false, true)) {
            return;
        }
        Register zkRegister = new ZookeeperRegister(agentConfig);
        registers.put(zkRegister.getName(), zkRegister);
    }

    /**
     * 获取注册器
     *
     * @param registerName
     * @return
     */
    public static Register getRegister(String registerName) {
        return registers.get(registerName);
    }
}
