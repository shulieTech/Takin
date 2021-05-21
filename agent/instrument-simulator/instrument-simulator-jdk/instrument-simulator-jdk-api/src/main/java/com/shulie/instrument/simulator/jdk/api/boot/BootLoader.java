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
package com.shulie.instrument.simulator.jdk.api.boot;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Boot 加载器,用于加载 jdk 自带的资源
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:37 下午
 */
public interface BootLoader {
    /**
     * 获取所有的资源
     *
     * @param name 资源名称
     * @return URL 集合
     * @throws IOException
     */
    Enumeration<URL> findResources(String name) throws IOException;

    /**
     * 获取单个资源
     *
     * @param name 资源名称
     * @return URL
     */
    URL findResource(String name);

    /**
     * 添加资源
     *
     * @param url
     */
    void addResource(URL url);

    /**
     * 查找 Class
     *
     * @param classLoader ClassLoader
     * @param name        class名称
     * @return Class
     */
    Class<?> findBootstrapClassOrNull(ClassLoader classLoader, String name) throws ClassNotFoundException;
}
