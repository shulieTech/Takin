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
package com.shulie.instrument.simulator.core.javamodule;

import com.shulie.instrument.simulator.compatible.module.JavaModule;
import com.shulie.instrument.simulator.compatible.module.JavaModuleFactory;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;

/**
 * JavaModuleFactory 的查找器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 10:10 下午
 */
public final class JavaModuleFactoryFinder {
    private final static String JAVA_MODULE_FACTORY_IMPL_CLASS = "com.shulie.instrument.simulator.compatible.jdk9.module.DefaultJavaModuleFactory";

    private JavaModuleFactoryFinder() {
    }

    /**
     * 查询对应的 JavaModuleFactory 的实现
     *
     * @param instrumentation instrumentation
     * @return JavaModuleFactory 的实现
     */
    public static JavaModuleFactory lookup(Instrumentation instrumentation) {
        if (instrumentation == null) {
            throw new NullPointerException("instrumentation");
        }

        final Class<JavaModuleFactory> javaModuleFactory = getJavaModuleFactory();
        try {
            Constructor<JavaModuleFactory> constructor = javaModuleFactory.getDeclaredConstructor(Instrumentation.class);
            return constructor.newInstance(instrumentation);
        } catch (Exception e) {
            throw new IllegalStateException("JavaModuleFactory() invoke fail Caused by:" + e.getMessage(), e);
        }
    }

    /**
     * 获取 JavaModuleFactory 实现类的 Class
     *
     * @return JavaModuleFactory 实现类的 Class
     */
    private static Class<JavaModuleFactory> getJavaModuleFactory() {
        try {
            return (Class<JavaModuleFactory>) Class.forName(JAVA_MODULE_FACTORY_IMPL_CLASS, false, JavaModule.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(JAVA_MODULE_FACTORY_IMPL_CLASS + " not found");
        }
    }
}
