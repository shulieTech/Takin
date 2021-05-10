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
package com.shulie.instrument.simulator.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * JDK Delegate ClassLoader, parent is excClassLoader, urls are jdk related path on SystemClassLoader
 */
public class JDKDelegateClassLoader extends URLClassLoader {
    static {
        try {
            ClassLoader.registerAsParallelCapable();
        } catch (Throwable e) {
            //ignore
        }
    }

    public JDKDelegateClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
