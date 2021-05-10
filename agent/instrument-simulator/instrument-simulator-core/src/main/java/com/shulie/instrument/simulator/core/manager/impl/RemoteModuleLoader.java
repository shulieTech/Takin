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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.shulie.instrument.simulator.api.ModuleRepositoryMode;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.core.manager.ModuleLoader;
import com.shulie.instrument.simulator.core.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 3:53 下午
 */
public class RemoteModuleLoader implements ModuleLoader {
    private final static Logger LOGGER = LoggerFactory.getLogger(RemoteModuleLoader.class);
    private final static String LOAD_ALLOW_MODULES_PATH = "/app/module/enabled";
    private final static String LOAD_MODULE_PATH = "/app/module/download";


    @Override
    public ModuleRepositoryMode supportMode() {
        return ModuleRepositoryMode.REMOTE;
    }

    @Override
    public File[] loadModuleLibs(String appName, String[] paths) {
        if (paths == null || paths.length == 0) {
            throw new RuntimeException("remote module repository url is not assigned.");
        }

        String url = StringUtils.trim(paths[0]);
        List<String> allowModules = loadAllowModules(appName, url + LOAD_ALLOW_MODULES_PATH);
        if (CollectionUtils.isEmpty(allowModules)) {
            LOGGER.warn("SIMULATOR: load allow module list from remote module repository got a empty Collection.");
            return new File[0];
        }
        List<File> files = new ArrayList<File>();
        for (String module : allowModules) {
            try {
                File moduleJarFile = File.createTempFile(module + "_" + System.nanoTime(), ".jar");
                HttpUtils.downloadFile(url + LOAD_MODULE_PATH + "?module=" + module + "&appName=" + appName, moduleJarFile);
                files.add(moduleJarFile);
                moduleJarFile.deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        return files.toArray(new File[files.size()]);
    }

    private List<String> loadAllowModules(String appName, String url) {
        String response = HttpUtils.doGet(url + "?appName=" + appName);
        if (response == null) {
            throw new RuntimeException("load remote module repository allow module list failed.");
        }
        List<String> allowModules = JSON.parseObject(response, new TypeReference<List<String>>() {
        }.getType());
        return allowModules;
    }
}
