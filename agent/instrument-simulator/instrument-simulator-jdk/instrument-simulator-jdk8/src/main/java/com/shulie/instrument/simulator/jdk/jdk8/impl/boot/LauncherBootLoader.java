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
package com.shulie.instrument.simulator.jdk.jdk8.impl.boot;

import com.shulie.instrument.simulator.jdk.api.boot.BootLoader;
import sun.misc.Launcher;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

/**
 * launcher boot 类加载器,包含 sun hotspot、oracle jdk 和 open jdk 的实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:40 下午
 */
public class LauncherBootLoader implements BootLoader {

    private final URLClassPath bootstrapClassPath = getBootstrapClassPath();

    private static final Method FIND_BOOTSTRAP_CLASS_OR_NULL = findBootstrapClassOrNullMethod();

    private static Method findBootstrapClassOrNullMethod() {
        // findBootstrapClassOrNull() jdk Compatibility
        // oracleJdk
        // oracleJdk 9~10 : reflection is not recommended

        // old OracleJdk :
        // oracleJdk6_24 : success

        // openjdk
        // openjdk8 : success
        // openjdk7 : success
        // openjdk6 : fail (findBootstrapClass0)
        NoSuchMethodException rootException;
        try {
            Method findBootstrapClassOrNull = ClassLoader.class.getDeclaredMethod("findBootstrapClassOrNull", String.class);
            findBootstrapClassOrNull.setAccessible(true);
            return findBootstrapClassOrNull;
        } catch (NoSuchMethodException ex) {
            rootException = ex;
        }
        try {
            // for openjdk6
            Method findBootstrapClass0 = ClassLoader.class.getDeclaredMethod("findBootstrapClass0", String.class);
            findBootstrapClass0.setAccessible(true);
            return findBootstrapClass0;
        } catch (NoSuchMethodException ignore) {
            // skip
        }
        throw new IllegalStateException("ClassLoader.findBootstrapClassOrNull api not found", rootException);
    }

    static {
        // preload
        new URLEnumeration(null);
    }

    public LauncherBootLoader() {
    }

    private static URLClassPath getBootstrapClassPath() {
        return Launcher.getBootstrapClassPath();
    }

    @Override
    public URL findResource(String name) {
        final Resource res = bootstrapClassPath.getResource(name);
        if (res == null) {
            return null;
        }
        return res.getURL();
    }

    @Override
    public void addResource(URL url) {
        bootstrapClassPath.addURL(url);
    }

    @Override
    public Class<?> findBootstrapClassOrNull(ClassLoader classLoader, String name) {
        try {
            return (Class<?>) FIND_BOOTSTRAP_CLASS_OR_NULL.invoke(classLoader, name);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(FIND_BOOTSTRAP_CLASS_OR_NULL.getName() + "() access fail " + ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof ClassNotFoundException) {
                // fix openjdk6
                return null;
            }
            throw new IllegalStateException(FIND_BOOTSTRAP_CLASS_OR_NULL.getName() + "() internal error " + ex.getMessage(), ex);
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        final Enumeration<Resource> enumeration = bootstrapClassPath.getResources(name);
        return new URLEnumeration(enumeration);
    }

    private static class URLEnumeration implements Enumeration<URL> {
        private final Enumeration<Resource> enumeration;

        private URLEnumeration(Enumeration<Resource> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public URL nextElement() {
            Resource resource = enumeration.nextElement();
            return resource.getURL();
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }
    }
}

