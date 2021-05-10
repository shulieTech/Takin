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
package com.shulie.instrument.simulator.compatible.jdk9.module;

import com.shulie.instrument.simulator.compatible.module.JavaModule;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;
import jdk.internal.module.Modules;

import java.lang.instrument.Instrumentation;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/14 10:06 上午
 */
public class ModuleSupport {

    /**
     * 添加 agent 模块的导出
     *
     * @param classLoader
     * @param inst
     */
    public static void prepareModule(ClassLoader classLoader, Instrumentation inst) {
        /**
         * 处理 agent 模块对于扩展模块的开放
         */
        JavaModule agentModule = wrapJavaModule(ModuleSupport.class, inst);
        final JavaModule unnamedModule = wrapJavaModule(classLoader.getUnnamedModule(), inst);
        agentModule.addAllExports(unnamedModule);
        final JavaModule prefsModule = wrapJavaModule(Modules.loadModule("java.prefs"), inst);
        agentModule.addReads(prefsModule);

        /**
         * 处理java base 模块对于扩展模块的开放
         */
        JavaModule baseModule = wrapJavaModule(Object.class, inst);
        baseModule.addOpens("java.net", unnamedModule);
        baseModule.addOpens("java.nio", unnamedModule);
        baseModule.addOpens("java.io", unnamedModule);
        baseModule.addOpens("java.math", unnamedModule);
        baseModule.addOpens("java.security", unnamedModule);
        baseModule.addOpens("java.text", unnamedModule);
        baseModule.addOpens("java.time", unnamedModule);
        baseModule.addOpens("java.util", unnamedModule);

        JavaModule javaSqlModule = wrapJavaModule(Modules.loadModule("java.sql"), inst);
        javaSqlModule.addOpens("java.sql", unnamedModule);
        javaSqlModule.addOpens("javax.sql", unnamedModule);

        JavaModule javaXml = wrapJavaModule(Modules.loadModule("java.xml"), inst);
        javaXml.addOpens("javax.xml", unnamedModule);

        // for Java9DefineClass
        baseModule.addExports("jdk.internal.misc", unnamedModule);
        baseModule.addExports("jdk.internal.module", unnamedModule);
        final JvmVersion version = JvmUtils.getVersion();
        if (version.onOrAfter(JvmVersion.JAVA_11)) {
            final String internalAccessModule = "jdk.internal.access";
            if (baseModule.getPackages().contains(internalAccessModule)) {
                baseModule.addExports(internalAccessModule, unnamedModule);
            }
        }
    }

    /**
     * 根据类包装成模块
     *
     * @param clazz
     * @return
     */
    private static JavaModule wrapJavaModule(Class clazz, Instrumentation instrumentation) {
        return new Java9Module(instrumentation, clazz.getModule());
    }

    /**
     * 包装成自定义的 JavaModule
     *
     * @param module 模块
     * @return JavaModule
     */
    private static JavaModule wrapJavaModule(Module module, Instrumentation instrumentation) {
        return new Java9Module(instrumentation, module);
    }
}
