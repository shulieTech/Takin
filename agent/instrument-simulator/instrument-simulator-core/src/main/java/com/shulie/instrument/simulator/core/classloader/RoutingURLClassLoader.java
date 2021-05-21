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

import com.shulie.instrument.simulator.core.util.CompoundEnumeration;
import com.shulie.instrument.simulator.core.util.EmptyEnumeration;
import com.shulie.instrument.simulator.jdk.api.boot.BootLoader;
import com.shulie.instrument.simulator.jdk.impl.boot.BootLoaderFactory;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 可路由的URLClassLoader
 */
public class RoutingURLClassLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(RoutingURLClassLoader.class);
    protected final ClassLoadingLock classLoadingLock = new ClassLoadingLock();
    private final Routing[] routingArray;
    protected final BootLoader bootLoader = BootLoaderFactory.newBootLoader();
    protected final ClassLoader parent;

    public RoutingURLClassLoader(final URL[] urls,
                                 final Routing... routingArray) {
        /**
         * 如果不指定 parent 则默认为 SystemClassLoader，这个地方显示指定为 null
         * 防止由于 使用 SystemClassLoader 先于我们的 ClassLoader 加载了某些共同的类，
         * 这样在我们内部用到了这个类会直接使用 SystemClassLoader 加载的类导致类冲突问题
         */
        super(urls, null);
        this.routingArray = routingArray;
        this.parent = null;
    }

    public RoutingURLClassLoader(final URL[] urls,
                                 final ClassLoader parent,
                                 final Routing... routingArray) {
        super(urls, parent);
        this.routingArray = routingArray;
        this.parent = parent;
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (null != url) {
            return url;
        }
        return getResourceInternal(name);
    }

    /**
     * Real logic to get resource
     *
     * @param name 资源名称
     * @return 返回资源URL
     */
    protected URL getResourceInternal(String name) {
        // 1. find routing resource
        URL url = getRoutingResource(name);

        // 2. find jdk resource
        if (url == null) {
            url = getJdkResource(name);
        }

        // 3. get local resource
        if (url == null) {
            url = getLocalResource(name);
        }

        return url;
    }

    protected URL getRoutingResource(String resourceName) {
        if (resourceName == null) {
            return null;
        }

        String className = StringUtils.replace(resourceName, "/", ".");
        if (className.endsWith(".class")) {
            className = StringUtils.substring(className, 0, StringUtils.lastIndexOf(className, ".class"));
        }
        // 优先查询类加载路由表,如果命中路由规则,则优先从路由表中的ClassLoader完成类加载
        if (ArrayUtils.isNotEmpty(routingArray)) {
            for (final Routing routing : routingArray) {
                if (!routing.isHit(className)) {
                    continue;
                }
                final ClassLoader routingClassLoader = routing.classLoader;
                try {
                    URL url = routingClassLoader.getResource(resourceName);
                    if (url == null) {
                        continue;
                    }
                    return url;
                } catch (Exception cause) {
                    // 如果在当前routingClassLoader中找不到应该优先加载的类(应该不可能，但不排除有就是故意命名成同名类)
                    // 此时应该忽略异常，继续往下加载
                    // ignore...
                }
            }
        }
        return null;
    }

    /**
     * Find jdk dir resource
     *
     * @param resourceName 资源名称
     * @return 返回jdk资源的url
     */
    protected URL getJdkResource(String resourceName) {
        return bootLoader.findResource(resourceName);
    }

    /**
     * Find local resource
     *
     * @param resourceName 资源名称
     * @return 返回资源URL
     */
    protected URL getLocalResource(String resourceName) {
        return super.getResource(resourceName);
    }


    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> urls = findResources(name);
        if (null != urls) {
            return urls;
        }
        return getResourcesInternal(name);
    }

    /**
     * Real logic to get resources
     *
     * @param name 资源名称
     * @return 返回资源列表
     * @throws IOException 资源加载不到会抛出IOException
     */
    protected Enumeration<URL> getResourcesInternal(String name) throws IOException {
        List<Enumeration<URL>> enumerationList = new ArrayList<Enumeration<URL>>();
        // 1. find routing resources
        enumerationList.add(getRoutingResources(name));
        // 2. find jdk resources
        enumerationList.add(getJdkResources(name));

        // 3. find local resources
        enumerationList.add(getLocalResources(name));


        return new CompoundEnumeration<URL>(
                enumerationList.toArray((Enumeration<URL>[]) new Enumeration<?>[0]));
    }

    protected Enumeration<URL> getLocalResources(String resourceName) throws IOException {
        return super.getResources(resourceName);
    }

    protected Enumeration<URL> getJdkResources(String resourceName) throws IOException {
        return bootLoader.findResources(resourceName);
    }

    protected Enumeration<URL> getRoutingResources(String resourceName) throws IOException {
        if (resourceName == null) {
            return new EmptyEnumeration<URL>();
        }

        List<Enumeration<URL>> list = new ArrayList<Enumeration<URL>>();
        String className = StringUtils.replace(resourceName, "/", ".");
        if (className.endsWith(".class")) {
            className = StringUtils.substring(className, 0, StringUtils.lastIndexOf(className, ".class"));
        }
        // 优先查询类加载路由表,如果命中路由规则,则优先从路由表中的ClassLoader完成类加载
        if (ArrayUtils.isNotEmpty(routingArray)) {
            for (final Routing routing : routingArray) {
                if (!routing.isHit(className)) {
                    continue;
                }
                final ClassLoader routingClassLoader = routing.classLoader;
                try {
                    Enumeration<URL> enumerations = routingClassLoader.getResources(resourceName);
                    if (enumerations.hasMoreElements()) {
                        list.add(enumerations);
                    }
                } catch (Exception cause) {
                    // 如果在当前routingClassLoader中找不到应该优先加载的类(应该不可能，但不排除有就是故意命名成同名类)
                    // 此时应该忽略异常，继续往下加载
                    // ignore...
                }
            }
        }
        return new CompoundEnumeration<URL>(list.toArray(new Enumeration[list.size()]));
    }

    /**
     * 从 Routing 中加载
     *
     * @param javaClassName
     * @param resolve
     * @return
     */
    protected Class<?> resolveRouting(final String javaClassName, final boolean resolve) {
        // 优先查询类加载路由表,如果命中路由规则,则优先从路由表中的ClassLoader完成类加载
        if (ArrayUtils.isNotEmpty(routingArray)) {
            for (final Routing routing : routingArray) {
                if (!routing.isHit(javaClassName)) {
                    continue;
                }
                final ClassLoader routingClassLoader = routing.classLoader;
                try {
                    return routingClassLoader.loadClass(javaClassName);
                } catch (Throwable cause) {
                    // 如果在当前routingClassLoader中找不到应该优先加载的类(应该不可能，但不排除有就是故意命名成同名类)
                    // 此时应该忽略异常，继续往下加载
                    // ignore...
                }
            }
        }
        return null;
    }

    /**
     * Load JDK class
     *
     * @param name class name
     * @return 返回尝试使用jdk类加载器加载的类
     */
    protected Class<?> resolveJDKClass(String name) {
        try {
            return bootLoader.findBootstrapClassOrNull(this, name);
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't resolve class {} from jdk with classloader {}.", name, this, e);
            }
            // ignore
        }
        return null;
    }

    /**
     * Load classpath class
     *
     * @param name className
     * @return resolved
     */
    protected Class<?> resolveLocalClass(final String name) {
        try {
            Class clazz = findLoadedClass(name);
            if (clazz != null) {
                return clazz;
            }
            return findClass(name);
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't resolve class {} from local with classloader {}.", name, this, e);
            }
            // ignore
        }
        return null;
    }

    /**
     * Load classpath class
     *
     * @param name    className
     * @param resolve 是否resolve
     * @return resolved
     */
    protected Class<?> resolveSystemClass(final String name, final boolean resolve) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            if (classLoader != null) {
                return classLoader.loadClass(name);
            }
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't resolve class {} from super with classloader {}.", name, this, e);
            }
            // ignore
        }
        return null;
    }

    /**
     * Load class from context class loader
     *
     * @param name    className
     * @param resolve 是否resolve
     * @return resolved
     */
    protected Class<?> resolveCurrentClass(final String name, final boolean resolve) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't resolve class {} from with context classloader {}.", name, this, e);
            }
            // ignore
        }
        return null;
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        return classLoadingLock.loadingInLock(name, new ClassLoadingLock.ClassLoading() {
            @Override
            public Class<?> loadClass(String javaClassName) throws ClassNotFoundException {
                Class<?> clazz = null;

                // 1. find routing
                if (clazz == null) {
                    clazz = resolveRouting(name, resolve);
                }

                // 2. findLoadedClass
                if (clazz == null) {
                    clazz = findLoadedClass(name);
                }
                // 3. JDK related class
                if (clazz == null) {
                    clazz = resolveJDKClass(name);
                }

                // 4. module classpath class
                if (clazz == null) {
                    clazz = resolveLocalClass(name);
                }

                // 5. super classpath class
                if (clazz == null) {
                    clazz = resolveSystemClass(name, resolve);
                }

                if (clazz != null) {
                    if (resolve) {
                        resolveClass(clazz);
                    }
                    return clazz;
                }

                throw new ClassNotFoundException("class " + name + " not found");
            }
        });

    }


    /**
     * 类加载路由匹配器
     */
    public static class Routing {

        protected final Collection<String/*REGEX*/> regexExpresses = new ArrayList<String>();
        protected final ClassLoader classLoader;

        /**
         * 构造类加载路由匹配器
         *
         * @param classLoader       目标ClassLoader
         * @param regexExpressArray 匹配规则表达式数组
         */
        Routing(final ClassLoader classLoader, final String... regexExpressArray) {
            if (ArrayUtils.isNotEmpty(regexExpressArray)) {
                regexExpresses.addAll(Arrays.asList(regexExpressArray));
            }
            this.classLoader = classLoader;
        }

        /**
         * 当前参与匹配的Java类名是否命中路由匹配规则
         * 命中匹配规则的类加载,将会从此ClassLoader中完成对应的加载行为
         *
         * @param javaClassName 参与匹配的Java类名
         * @return true:命中;false:不命中;
         */
        protected boolean isHit(final String javaClassName) {
            for (final String regexExpress : regexExpresses) {
                try {
                    if (javaClassName.matches(regexExpress)) {
                        return true;
                    }
                } catch (Throwable cause) {
                    logger.warn("SIMULATOR: routing {} failed, regex-express={}.", javaClassName, regexExpress, cause);
                }
            }
            return false;
        }

    }

}
