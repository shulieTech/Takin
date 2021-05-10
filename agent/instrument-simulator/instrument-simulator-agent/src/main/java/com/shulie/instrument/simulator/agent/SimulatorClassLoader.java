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
package com.shulie.instrument.simulator.agent;


import com.shulie.instrument.simulator.message.boot.BootLoader;
import com.shulie.instrument.simulator.message.boot.BootLoaderFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

/**
 * 加载Simulator用的ClassLoader
 */
class SimulatorClassLoader extends URLClassLoader {
    private final String toString;
    private final String path;
    /**
     * 兼容 jvm 多版本，多 jvm 类型的情况,也是为了与应用加载的类做隔离，不允许框架内部的类查找到应用上面
     */
    private BootLoader bootLoader;

    private final static List<String> PREFIX_CLASS_OF_LOAD_PARENTS = Arrays.asList(
            "javax.",
            "java.",
            "sun.",
            "com.sun."
    );

    private final static List<String> PREFIX_RESOURCE_OF_LOAD_PARENTS = Arrays.asList(
            "javax/",
            "java/",
            "sun/",
            "com/sun/"
    );

    SimulatorClassLoader(final String namespace,
                         final String simulatorCoreJarFilePath) throws MalformedURLException {
        super(new URL[]{new URL("file:" + simulatorCoreJarFilePath)});
        this.path = simulatorCoreJarFilePath;
        this.toString = String.format("SimulatorClassLoader[namespace=%s;path=%s;]", namespace, path);
    }

    private void initBootLoader() {
        if (bootLoader == null) {
            synchronized (this) {
                if (bootLoader == null) {
                    bootLoader = BootLoaderFactory.newBootLoader();
                }
            }
        }
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (null != url) {
            return url;
        }
        initBootLoader();
        url = bootLoader.findResource(name);
        if (url == null) {
            url = loadResourceOfParent(name);
        }
        return url;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> urls = findResources(name);
        if (null != urls) {
            return urls;
        }
        initBootLoader();
        urls = bootLoader.findResources(name);
        if (urls == null) {
            urls = loadResourcesOfParent(name);
        }
        return urls;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Throwable t) {
            try {
                initBootLoader();
                Class clazz = bootLoader.findBootstrapClassOrNull(this, name);
                /**
                 * 对于有一些 jdk 中的类如jdbc，可能是由 AppClassLoader 加载的，此时需要父类加载加载
                 */
                if (clazz == null) {
                    clazz = loadClassOfParent(name, resolve);
                }
                return clazz;
            } catch (ClassNotFoundException e) {
                throw e;
            } catch (Throwable e) {
                throw new ClassNotFoundException("", e);
            }
        }
    }

    private Class loadClassOfParent(String name, boolean resolve) throws ClassNotFoundException {
        for (String prefix : PREFIX_CLASS_OF_LOAD_PARENTS) {
            if (name.startsWith(prefix)) {
                return super.loadClass(name, resolve);
            }
        }
        return null;
    }

    private URL loadResourceOfParent(String name) {
        for (String prefix : PREFIX_RESOURCE_OF_LOAD_PARENTS) {
            if (name.startsWith(prefix)) {
                return super.getResource(name);
            }
        }
        return null;
    }

    private Enumeration<URL> loadResourcesOfParent(String name) throws IOException {
        for (String prefix : PREFIX_RESOURCE_OF_LOAD_PARENTS) {
            if (name.startsWith(prefix)) {
                return super.getResources(name);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return toString;
    }


    /**
     * 尽可能关闭ClassLoader
     * <p>
     * URLClassLoader会打开指定的URL资源，在Simulator中则是对应的Jar文件，如果不在shutdown的时候关闭ClassLoader，会导致下次再次加载
     * 的时候，依然会访问到上次所打开的文件（底层被缓存起来了）
     * <p>
     * 在JDK1.7版本中，URLClassLoader提供了{@code close()}方法来完成这件事；但在JDK1.6版本就要下点手段了；
     * <p>
     * 该方法将会被{@code ControlModule#shutdown}通过反射调用，
     * 请保持方法声明一致
     */
    @SuppressWarnings("unused")
    public void closeIfPossible() {

        // 如果是JDK7+的版本, URLClassLoader实现了Closeable接口，直接调用即可
        if (this instanceof Closeable) {
            try {
                ((Closeable) this).close();
            } catch (Throwable cause) {
                // ignore...
            }
            return;
        }


        // 对于JDK6的版本，URLClassLoader要关闭起来就显得有点麻烦，这里弄了一大段代码来稍微处理下
        // 而且还不能保证一定释放干净了，至少释放JAR文件句柄是没有什么问题了
        try {
            final Object ucp = forceGetDeclaredFieldValue(URLClassLoader.class, "ucp", this);
            final Object loaders = forceGetDeclaredFieldValue(ucp.getClass(), "loaders", ucp);

            for (final Object loader :
                    ((Collection) loaders).toArray()) {
                try {
                    final JarFile jarFile = forceGetDeclaredFieldValue(loader.getClass(), "jar", loader);
                    jarFile.close();
                } catch (Throwable t) {
                    // if we got this far, this is probably not a JAR loader so skip it
                }
            }

        } catch (Throwable cause) {
            // ignore...
        }

    }

    private <T> T forceGetDeclaredFieldValue(Class<?> clazz, String name, Object target) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(target);
    }

}
