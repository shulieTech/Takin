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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.ModuleRepositoryMode;
import com.shulie.instrument.simulator.core.manager.ModuleLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 3:36 下午
 */
public class LocalModuleLoader implements ModuleLoader {

    @Override
    public ModuleRepositoryMode supportMode() {
        return ModuleRepositoryMode.LOCAL;
    }

    @Override
    public File[] loadModuleLibs(String appName, String[] paths) {
        final Collection<File> foundModuleJarFiles = new LinkedHashSet<File>();
        for (final String path : paths) {
            final File fileOfPath = new File(path);
            if (fileOfPath.isDirectory()) {
                foundModuleJarFiles.addAll(FileUtils.listFiles(new File(path), new String[]{"jar"}, true));
            } else {
                if (StringUtils.endsWithIgnoreCase(fileOfPath.getPath(), ".jar")) {
                    foundModuleJarFiles.add(fileOfPath);
                }
            }
        }
        return foundModuleJarFiles.toArray(new File[foundModuleJarFiles.size()]);
    }
}
