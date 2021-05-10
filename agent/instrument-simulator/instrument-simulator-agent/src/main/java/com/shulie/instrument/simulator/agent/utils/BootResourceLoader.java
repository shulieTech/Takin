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
package com.shulie.instrument.simulator.agent.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/30 9:59 下午
 */
public class BootResourceLoader {
    // java 版本号
    private static final String JAVA_VERSION_KEY = "java.specification.version";
    // java 1.8版本号
    private static final float JAVA_8_VERSION = 1.8f;

    public static void addResource(List<File> bootstrapFiles) throws MalformedURLException {
        float jvmVersion = getJvmVersion();
        if (jvmVersion > JAVA_8_VERSION) {
            return;
        }
        sun.misc.URLClassPath urlClassPath = getBootstrapClassPath();
        for (File file : bootstrapFiles) {
            urlClassPath.addURL(file.toURI().toURL());
        }
    }

    private static sun.misc.URLClassPath getBootstrapClassPath() {
        return sun.misc.Launcher.getBootstrapClassPath();
    }

    private static float getJvmVersion() {
        String jvmVersion = System.getProperty(JAVA_VERSION_KEY, "");
        try {
            return Float.parseFloat(jvmVersion);
        } catch (Throwable e) {
            return -1;
        }
    }
}
