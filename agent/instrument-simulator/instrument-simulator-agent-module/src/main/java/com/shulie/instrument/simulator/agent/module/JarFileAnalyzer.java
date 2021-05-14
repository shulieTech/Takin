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


import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * jar 文件分析器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public class JarFileAnalyzer implements PackageAnalyzer {

    private static final String META_INF = "META-INF/";

    private static final String CLASS_EXTENSION = ".class";

    private static final String SERVICE_LOADER = META_INF + "services/";

    private static final List<String> SKIP_INVALID_LIST = Arrays.asList(
            SERVICE_LOADER + "org.codehaus.groovy.runtime.ExtensionModule",
            SERVICE_LOADER + "org.codehaus.groovy.source.Extensions"
    );
    /**
     * jar 文件
     */
    private final JarFile jarFile;
    /**
     * jar entry 过滤器
     */
    private final JarEntryFilter filter;
    /**
     * ServiceLoader 的 Entry 过滤器
     */
    private final ServiceLoaderEntryFilter serviceLoaderEntryFilter;

    JarFileAnalyzer(JarFile jarFile) {
        this.jarFile = jarFile;
        this.filter = new PackageFilter();
        this.serviceLoaderEntryFilter = new DefaultServiceLoaderEntryFilter();
    }

    @Override
    public PackageInfo analyze() {
        Set<String> packageSet = new HashSet<>();
        List<Providers> providesList = new ArrayList<>();

        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();

            String packageName = this.filter.filter(jarEntry);
            if (packageName != null) {
                // 如果包名是非法的，则取上一级包名
                if (packageName.endsWith(".enum") || packageName.contains(".enum.")) {
                    packageName = packageName.substring(0, packageName.indexOf(".enum"));
                }
                packageSet.add(packageName);
            }

            final Providers provides = this.serviceLoaderEntryFilter.filter(jarEntry);
            if (provides != null) {
                providesList.add(provides);
            }
        }
        return new PackageInfo(packageSet, providesList);
    }

    /**
     * ServiceLoader 的 Entry 过滤器
     */
    interface ServiceLoaderEntryFilter {
        /**
         * 根据 JarEntry 过滤出 Providers
         *
         * @param jarEntry
         * @return
         */
        Providers filter(JarEntry jarEntry);
    }

    /**
     * 默认的 ServiceLoader 的 Entry 过滤器实现
     */
    class DefaultServiceLoaderEntryFilter implements ServiceLoaderEntryFilter {
        @Override
        public Providers filter(JarEntry jarEntry) {
            final String jarEntryName = jarEntry.getName();
            if (!jarEntryName.startsWith(SERVICE_LOADER)) {
                return null;
            }
            if (jarEntry.isDirectory()) {
                return null;
            }
            if (jarEntryName.indexOf('/', SERVICE_LOADER.length()) != -1) {
                return null;
            }
            if (SKIP_INVALID_LIST.contains(jarEntryName)) {
                return null;
            }
            try {
                InputStream inputStream = jarFile.getInputStream(jarEntry);

                ServiceDescriptorParser parser = new ServiceDescriptorParser();
                List<String> serviceImplClassName = parser.parse(inputStream);
                String serviceClassName = jarEntryName.substring(SERVICE_LOADER.length());
                return new Providers(serviceClassName, serviceImplClassName);
            } catch (IOException e) {
                throw new IllegalStateException(jarFile.getName() + " File read fail ", e);
            }
        }

    }

    /**
     * JarEntry 过滤器
     */
    interface JarEntryFilter {
        /**
         * 过滤
         *
         * @param jarEntry
         * @return
         */
        String filter(JarEntry jarEntry);
    }

    /**
     * 包过滤器
     */
    static class PackageFilter implements JarEntryFilter {
        @Override
        public String filter(JarEntry jarEntry) {
            if (jarEntry.getName().startsWith(META_INF)) {
                // skip META-INF
                return null;
            }
            if (jarEntry.isDirectory()) {
                // skip empty dir
                return null;
            }

            final String fileName = jarEntry.getName();
            if (!checkFIleExtension(fileName, CLASS_EXTENSION)) {
                // skip non class file
                return null;
            }

            final String packageName = getPackageName(fileName, '/', null);
            if (packageName == null) {
                return null;
            }
            return toPackageName(packageName);
        }

        private boolean checkFIleExtension(String fileName, String extension) {
            return fileName.endsWith(extension);
        }

        private static String getPackageName(String fqcn, char packageSeparator, String defaultValue) {
            if (fqcn == null) {
                throw new NullPointerException("fully-qualified class name");
            }
            final int lastPackageSeparatorIndex = fqcn.lastIndexOf(packageSeparator);
            if (lastPackageSeparatorIndex == -1) {
                return defaultValue;
            }
            return fqcn.substring(0, lastPackageSeparatorIndex);
        }

        private String toPackageName(String dirFormat) {
            if (dirFormat == null) {
                return null;
            }
            return dirFormat.replace('/', '.');
        }
    }
}
