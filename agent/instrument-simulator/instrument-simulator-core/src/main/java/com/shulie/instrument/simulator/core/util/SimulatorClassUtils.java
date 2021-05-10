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
package com.shulie.instrument.simulator.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * Simulator的类操作工具类
 */
public class SimulatorClassUtils {

    private static final String SIMULATOR_FAMILY_CLASS_RES_PREFIX = "com/shulie/instrument/simulator/";
    private static final String SIMULATOR_FAMILY_CLASS_PREFIX = SIMULATOR_FAMILY_CLASS_RES_PREFIX.replace('/', '.');

    /**
     * 是否是Simulator家族所管理的类
     * <p>
     * Simulator家族所管理的类包括：
     * 1. {@code com.shulie.instrument.simulator.}开头的类名
     * 2. 被{@code com.shulie.instrument.simulator.}开头的ClassLoader所加载的类
     * </p>
     *
     * @param internalClassName 类资源名
     * @param loader            加载类的ClassLoader
     * @return true:属于Simulator家族;false:不属于
     */
    public static boolean isComeFromSimulatorFamily(final String internalClassName, final ClassLoader loader) {

        // 类名是com.shulie.instrument.simulator开头
        if (null != internalClassName
                && isSimulatorPrefix(internalClassName)) {
            return true;
        }

        // 类被com.shulie.instrument.simulator开头的ClassLoader所加载
        if (null != loader
                && isSimulatorClassPrefix(loader.getClass().getName())) {
            return true;
        }

        return false;

    }

    /**
     * 标准化类名
     * <p>
     * 入参：com.shulie.instrument.simulator
     * 返回：com/shulie/instrument/simulator
     * </p>
     *
     * @param className 类名
     * @return 标准化类名
     */
    private static String normalizeClass(String className) {
        return StringUtils.replaceChars(className, '.', '/');
    }

    /**
     * 是否是Simulator自身的类
     * <p>
     * 需要注意internalClassName的格式形如: com/shulie/instrument/simulator/
     *
     * @param internalClassName 类资源名
     * @return true / false
     */
    private static boolean isSimulatorPrefix(String internalClassName) {
        return internalClassName.startsWith(SIMULATOR_FAMILY_CLASS_RES_PREFIX);
    }

    /**
     * 是否是 Simulator 自身的类
     * 需要注意internalClassName的格式形如: com.shulie.instrument.simulator.
     *
     * @param className
     * @return
     */
    private static boolean isSimulatorClassPrefix(String className) {
        return className.startsWith(SIMULATOR_FAMILY_CLASS_PREFIX);
    }
}
