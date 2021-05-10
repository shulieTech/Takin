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
package com.shulie.instrument.simulator.core.inject.util;

import java.util.jar.JarEntry;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class ExtensionFilter implements JarEntryFilter {

    public static final JarEntryFilter CLASS_FILTER = new ExtensionFilter(".class");

    private final String extension;

    public ExtensionFilter(String extension) {
        if (extension == null) {
            throw new NullPointerException("extension must not be null");
        }
        this.extension = extension;
    }

    @Override
    public boolean filter(JarEntry jarEntry) {
        final String name = jarEntry.getName();
        return name.endsWith(extension);
    }
}
