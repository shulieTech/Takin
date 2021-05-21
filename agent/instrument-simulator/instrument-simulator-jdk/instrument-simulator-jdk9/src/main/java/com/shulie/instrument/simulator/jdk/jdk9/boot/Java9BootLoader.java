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
package com.shulie.instrument.simulator.jdk.jdk9.boot;



import com.shulie.instrument.simulator.jdk.api.boot.BootLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:40 下午
 */
public class Java9BootLoader implements BootLoader {

    public Java9BootLoader() {
    }

    @Override
    public URL findResource(String name) {
        return jdk.internal.loader.BootLoader.findResource(name);
    }

    @Override
    public void addResource(URL url) {

    }

    @Override
    public Class<?> findBootstrapClassOrNull(ClassLoader classLoader, String name) {
        return jdk.internal.loader.BootLoader.loadClassOrNull(name);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        return jdk.internal.loader.BootLoader.findResources(name);
    }
}

