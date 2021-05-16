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

import com.shulie.instrument.simulator.api.ModuleSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * 模块信息加载工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/19 10:06 下午
 */
public final class ModuleSpecUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ModuleSpecUtils.class);

    public static ModuleSpec loadModuleSpec(File file, boolean isSystemModule) {
        if (file == null) {
            return null;
        }
        ModuleSpec moduleSpec = getModuleInfo(file);
        if (moduleSpec == null) {
            return null;
        }
        moduleSpec.setSystemModule(isSystemModule);
        return moduleSpec;
    }

    public static List<ModuleSpec> loadModuleSpecs(List<File> files, boolean isSystemModule) {
        if (CollectionUtils.isEmpty(files)) {
            return Collections.EMPTY_LIST;
        }
        List<ModuleSpec> moduleSpecs = new ArrayList<ModuleSpec>();
        for (File file : files) {
            ModuleSpec moduleSpec = getModuleInfo(file);
            if (moduleSpec != null) {
                moduleSpec.setSystemModule(isSystemModule);
                moduleSpecs.add(moduleSpec);
            }
        }
        Collections.sort(moduleSpecs, new Comparator<ModuleSpec>() {
            @Override
            public int compare(ModuleSpec o1, ModuleSpec o2) {
                if ((o1.isMiddlewareModule() && o2.isMiddlewareModule()) || (!o1.isMiddlewareModule() && !o2.isMiddlewareModule())) {
                    if (o1.getPriority() == o2.getPriority()) {
                        return 0;
                    }
                    return o1.getPriority() - o2.getPriority() > 0 ? 1 : -1;
                }
                if (o1.isMiddlewareModule()) {
                    return 1;
                }
                return -1;
            }
        });

        return moduleSpecs;
    }


    private static Set<String> splitAndTrim(String str, String separator) {
        if (StringUtils.isBlank(str)) {
            return Collections.EMPTY_SET;
        }
        String[] arr = StringUtils.split(str, separator);
        Set<String> result = new HashSet<String>();
        for (String value : arr) {
            if (StringUtils.isNotBlank(value)) {
                result.add(StringUtils.trim(value));
            }
        }
        return result;
    }

    private static ModuleSpec getModuleInfo(File file) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            ZipEntry jarEntry = jarFile.getEntry("module.config");
            if (jarEntry == null) {
                LOGGER.error("SIMULATOR: found a invalid module jar,can't found module.config : {} ", file.getAbsolutePath());
                return null;
            }
            InputStream in = null;
            try {
                in = jarFile.getInputStream(jarEntry);
                Properties properties = new Properties();
                properties.load(in);
                String version = properties.getProperty("simulator-version");
                String sinceVersion = null, untilVersion = null;
                if (StringUtils.isBlank(version)) {
                    sinceVersion = "1.0.0";
                    untilVersion = "1000.0.0";
                } else {
                    if (StringUtils.indexOf(version, '-') != -1) {
                        String[] arr = StringUtils.split(version, '-');
                        if (arr.length > 0) {
                            sinceVersion = StringUtils.trim(arr[0]);
                        }
                        if (arr.length > 1) {
                            untilVersion = StringUtils.trim(arr[1]);
                        } else {
                            untilVersion = "1000.0.0";
                        }
                    } else {
                        sinceVersion = StringUtils.trim(version);
                        untilVersion = sinceVersion;
                    }
                }

                if (StringUtils.isNotBlank(sinceVersion) && !VersionUtils.isValidVersion(sinceVersion)) {
                    LOGGER.error("SIMULATOR: read module {} err, simulator-version is valid: {} ", file.getName(), version);
                    return null;
                }

                if (StringUtils.isNotBlank(untilVersion) && !VersionUtils.isValidVersion(untilVersion)) {
                    LOGGER.error("SIMULATOR: read module {} err, simulator-version is valid: {} ", file.getName(), version);
                    return null;
                }

                String mustUseStr = properties.getProperty("must-use");
                boolean mustUse = Boolean.valueOf(mustUseStr);

                String moduleId = properties.getProperty("module-id");

                String middlewareModuleStr = properties.getProperty("middleware-module");

                ModuleSpec moduleSpec = new ModuleSpec();
                moduleSpec.setMustUse(mustUse)
                        .setModuleId(moduleId)
                        .setFile(file)
                        .setExportClasses(splitAndTrim(properties.getProperty("export-class"), ","))
                        .setImportClasses(splitAndTrim(properties.getProperty("import-class"), ","))
                        .setExportPackages(StringUtils.trim(properties.getProperty("export-package")))
                        .setImportPackages(StringUtils.trim(properties.getProperty("import-package")))
                        .setExportResources(StringUtils.trim(properties.getProperty("export-resource")))
                        .setImportResources(StringUtils.trim(properties.getProperty("import-resource")))
                        .setSinceVersion(sinceVersion)
                        .setUntilVersion(untilVersion);

                if (StringUtils.isNotBlank(middlewareModuleStr)) {
                    moduleSpec.setMiddlewareModule(Boolean.valueOf(middlewareModuleStr));
                }

                final String priority = properties.getProperty("priority", "0");
                moduleSpec.setDependencies(splitAndTrim(properties.getProperty("dependencies"), ","));
                if (NumberUtils.isDigits(priority)) {
                    moduleSpec.setPriority(Integer.valueOf(priority));
                }
                return moduleSpec;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("SIMULATOR: read module jar err:{} ", file.getAbsolutePath(), e);
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: load module from jar err:{} ", file.getAbsolutePath(), e);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    LOGGER.warn("[SIMULATOR: close module jar err:{} ", file.getAbsolutePath(), e);
                }
            }
        }
        return null;
    }

    public static Set<String> getAllModuleIds(List<ModuleSpec> moduleSpecs) {
        if (CollectionUtils.isEmpty(moduleSpecs)) {
            return Collections.EMPTY_SET;
        }
        Set<String> result = new HashSet<String>();
        for (ModuleSpec moduleSpec : moduleSpecs) {
            result.add(moduleSpec.getModuleId());
        }
        return result;
    }
}
