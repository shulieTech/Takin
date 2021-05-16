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
package com.shulie.instrument.simulator.agent.module;

import com.shulie.instrument.simulator.agent.module.exception.ModuleException;
import jdk.internal.loader.BootLoader;
import jdk.internal.module.Modules;

import java.io.Closeable;
import java.io.IOException;
import java.lang.module.ModuleDescriptor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

/**
 * 模块构建器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
class ModuleBuilder {

    private final ModuleLogger logger = ModuleLogger.getLogger(getClass().getName());

    boolean isExistsModule(String moduleName) {
        try {
            Module module = Modules.loadModule(moduleName);
            return module != null;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 定义模块
     *
     * @param moduleName  模块名称
     * @param classLoader 类加载器
     * @param urls        url 列表
     * @return 模块
     */
    Module defineModule(String moduleName, ClassLoader classLoader, URL[] urls) {
        if (moduleName == null) {
            throw new NullPointerException("moduleName");
        }
        if (urls == null) {
            throw new NullPointerException("urls");
        }
        if (urls.length == 0) {
            throw new IllegalArgumentException("urls.length is 0");
        }
        logger.info("bootstrap unnamedModule:" + BootLoader.getUnnamedModule());
        logger.info("platform unnamedModule:" + ClassLoader.getPlatformClassLoader().getUnnamedModule());
        logger.info("system unnamedModule:" + ClassLoader.getSystemClassLoader().getUnnamedModule());

        Module unnamedModule = classLoader.getUnnamedModule();
        logger.info("defineModule classLoader: " + classLoader);
        logger.info("defineModule classLoader-unnamedModule: " + unnamedModule);


        List<PackageInfo> packageInfos = parsePackageInfo(urls);
        Set<String> packages = mergePackageInfo(packageInfos);
        logger.info("packages:" + packages);
        Map<String, Set<String>> serviceInfoMap = mergeServiceInfo(packageInfos);
        logger.info("providers:" + serviceInfoMap);

        ModuleDescriptor.Builder builder = ModuleDescriptor.newModule(moduleName);
        builder.packages(packages);
        for (Map.Entry<String, Set<String>> entry : serviceInfoMap.entrySet()) {
            builder.provides(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        ModuleDescriptor moduleDescriptor = builder.build();
        URI url = getInformationURI(urls);

        Module module = Modules.defineModule(classLoader, moduleDescriptor, url);
        logger.info("defineModule module:" + module);
        return module;
    }

    /**
     * 合并服务信息
     *
     * @param packageInfos 包信息列表
     * @return
     */
    private Map<String, Set<String>> mergeServiceInfo(List<PackageInfo> packageInfos) {
        Map<String, Set<String>> providesMap = new HashMap<>();
        for (PackageInfo packageInfo : packageInfos) {
            List<Providers> serviceLoader = packageInfo.getProviders();
            for (Providers provides : serviceLoader) {
                Set<String> providerSet = providesMap.computeIfAbsent(provides.getService(), s -> new HashSet<>());
                providerSet.addAll(provides.getProviders());
            }
        }
        return providesMap;
    }

    /**
     * 合并包信息
     *
     * @param packageInfos
     * @return
     */
    private Set<String> mergePackageInfo(List<PackageInfo> packageInfos) {
        Set<String> packageSet = new HashSet<>();
        for (PackageInfo packageInfo : packageInfos) {
            packageSet.addAll(packageInfo.getPackage());
        }
        return packageSet;
    }

    /**
     * 新建 JarFile
     *
     * @param jarFile
     * @return
     */
    private JarFile newJarFile(URL jarFile) {
        try {
            if (!jarFile.getProtocol().equals("file")) {
                throw new IllegalStateException("invalid file " + jarFile);
            }
            return new JarFile(jarFile.getFile());
        } catch (IOException e) {
            throw new ModuleException(jarFile.getFile() + " create fail " + e.getMessage(), e);
        }
    }

    /**
     * getInformationURI
     *
     * @param urls
     * @return
     */
    private URI getInformationURI(URL[] urls) {
        if (isEmpty(urls)) {
            return null;
        }
        final URL url = urls[0];
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 判断 urls 是否为空
     *
     * @param urls
     * @return
     */
    private boolean isEmpty(URL[] urls) {
        return urls == null || urls.length == 0;
    }

    /**
     * 解析包信息
     *
     * @param urls urls
     * @return 返回包信息列表
     */
    private List<PackageInfo> parsePackageInfo(URL[] urls) {

        final List<PackageInfo> packageInfoList = new ArrayList<>();
        for (URL url : urls) {
            if (!isJar(url)) {
                continue;
            }
            JarFile jarFile = null;
            try {
                jarFile = newJarFile(url);
                PackageAnalyzer packageAnalyzer = new JarFileAnalyzer(jarFile);
                PackageInfo packageInfo = packageAnalyzer.analyze();
                packageInfoList.add(packageInfo);
            } finally {
                close(jarFile);
            }
        }
        return packageInfoList;
    }

    /**
     * 判断 url 是否是一个 jar
     *
     * @param url url
     * @return
     */
    private boolean isJar(URL url) {
        // filter *.xml
        if (url.getPath().endsWith(".jar")) {
            return true;
        }
        return false;
    }

    /**
     * 关闭 Closeable
     *
     * @param closeable
     */
    private void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignore) {
            // skip
        }
    }

}
