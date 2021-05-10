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
package com.shulie.instrument.simulator.module.heartbeat;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import org.kohsuke.MetaInfServices;

import java.util.Map;

/**
 * 心跳模块, 供外部检查是否存活
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 8:10 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "heartbeat", version = "1.0.0", author = "xiaobin@shulie.io", description = "心跳模块")
public class HeartbeatModule extends ParamSupported implements ExtensionModule {

    @Command(value = "info", description = "心跳命令")
    public CommandResponse info(final Map<String, String> args) {
        return CommandResponse.success(true);
    }
}
