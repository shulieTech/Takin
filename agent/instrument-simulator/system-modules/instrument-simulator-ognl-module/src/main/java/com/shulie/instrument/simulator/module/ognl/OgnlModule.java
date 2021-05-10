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
package com.shulie.instrument.simulator.module.ognl;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.express.Express;
import com.shulie.instrument.simulator.module.express.ExpressException;
import com.shulie.instrument.simulator.module.express.ExpressFactory;
import com.shulie.instrument.simulator.module.util.ClassLoaderUtils;
import com.shulie.instrument.simulator.module.util.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.instrument.Instrumentation;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/11 6:54 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "ognl", version = "1.0.0", author = "xiaobin@shulie.io", description = "ognl 模块")
public class OgnlModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(OgnlModule.class);
    @Resource
    private SimulatorConfig simulatorConfig;

    @Command( value = "info",description = "执行 ognl 表达式")
    public CommandResponse info(final Map<String, String> args) {
        String express = args.get("express");
        String hashCode = args.get("classLoader");
        if (StringUtils.isBlank(express)) {
            return CommandResponse.failure("express can't be null or empty.");
        }
        try {
            Instrumentation inst = simulatorConfig.getInstrumentation();
            ClassLoader classLoader = null;
            if (hashCode == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            } else {
                classLoader = ClassLoaderUtils.getClassLoader(inst, hashCode);
            }
            if (classLoader == null) {
                return CommandResponse.failure("Can not find classloader with classloader<hashCode>: " + hashCode + ".");
            }

            Express unpooledExpress = ExpressFactory.unpooledExpress(classLoader);

            Object value = unpooledExpress.get(express);
            return CommandResponse.success(value);
        } catch (ExpressException e) {
            logger.warn("ognl: failed execute express: " + express, e);
            return CommandResponse.failure("Failed to execute ognl, exception message: " + e.getMessage()
                    + ", please check $HOME/logs/simulator/simulator.log for more details. ");
        } catch (Throwable t) {
            logger.warn("ognl: failed execute express: " + express, t);
            return CommandResponse.failure(t);
        }
    }
}
