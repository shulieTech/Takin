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
package com.shulie.instrument.simulator.module.sysprop;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/12 9:46 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "sysprop", version = "1.0.0", author = "xiaobin@shulie.io", description = "系统变量模块")
public class SystemPropertyModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(SystemPropertyModule.class);

    @Command(value = "info", description = "查看系统变量")
    public CommandResponse info(final Map<String, String> args) {
        String propertyName = args.get("propertyName");
        try {
            return CommandResponse.success(getSystemProperties(propertyName));
        } catch (Throwable e) {
            logger.warn("sysprop: info system property err:{}", propertyName);
            return CommandResponse.failure(e);
        }
    }

    private Map<String, String> getSystemProperties(String key) {
        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isNotBlank(key)) {
            result.put(key, System.getProperty(key));
        } else {
            Properties props = System.getProperties();
            final Enumeration<?> propertyNames = props.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                result.put(propertyName, System.getProperty(propertyName));
            }
        }
        return result;
    }

    @Command(value = "setprop", description = "设置系统变量")
    public CommandResponse setProp(final Map<String, String> args) {
        String propertyName = args.get("propertyName");
        String propertyValue = args.get("propertyValue");
        try {

            if (StringUtils.isBlank(propertyName) || StringUtils.isBlank(propertyValue)) {
                return CommandResponse.failure("propertyName and propertyValue must not be empty." + propertyName + " = " + propertyValue);
            }
            System.setProperty(propertyName, propertyValue);
            return CommandResponse.success(Boolean.TRUE);
        } catch (Throwable e) {
            logger.warn("sysprop: set system property err:{}={}", propertyName, propertyValue);
            return CommandResponse.failure(e);
        }
    }
}
