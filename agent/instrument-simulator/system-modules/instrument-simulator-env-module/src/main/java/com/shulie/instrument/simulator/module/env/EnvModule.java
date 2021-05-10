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
package com.shulie.instrument.simulator.module.env;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/12 9:55 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "env", version = "1.0.0", author = "xiaobin@shulie.io", description = "环境变量模块")
public class EnvModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(EnvModule.class);

    @Command(value = "info", description = "查看环境变量信息")
    public CommandResponse info(final Map<String, String> args) {
        String propertyName = args.get("propertyName");
        try {
            return CommandResponse.success(getEnvProperties(propertyName));
        } catch (Throwable e) {
            logger.warn("env: info system property err:{}", propertyName);
            return CommandResponse.failure(e);
        }
    }

    private Map<String, String> getEnvProperties(String key) {
        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isNotBlank(key)) {
            result.put(key, System.getenv(key));
        } else {
            Map<String, String> props = System.getenv();
            result.putAll(props);
        }
        return result;
    }
}
