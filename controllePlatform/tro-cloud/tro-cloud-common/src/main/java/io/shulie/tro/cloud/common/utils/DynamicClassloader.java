/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.common.utils;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author shulie
 * @package: com.pamirs.tro.common.util
 * @Date 2019-06-26 15:32
 */
public class DynamicClassloader extends URLClassLoader {

    public DynamicClassloader() {
        this(new URL[] {});
    }

    public DynamicClassloader(URL[] urls) {
        super(urls);
    }

    public DynamicClassloader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

}
