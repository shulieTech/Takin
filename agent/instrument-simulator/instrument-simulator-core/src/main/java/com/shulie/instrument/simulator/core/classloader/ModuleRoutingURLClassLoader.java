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
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

/**
 * 模块类加载器的路由加载器实现
 */
public abstract class ModuleRoutingURLClassLoader extends RoutingURLClassLoader {
    protected static final String CLASS_RESOURCE_SUFFIX = ".class";

    private static final Logger logger = LoggerFactory.getLogger(ModuleRoutingURLClassLoader.class);
    private final Routing[] routingArray;
    protected final ClassLoaderService classLoaderService;
    protected final String moduleId;

    public ModuleRoutingURLClassLoader(final String moduleId,
                                       final ClassLoaderService classLoaderService,
                                       final URL[] urls,
                                       final Routing... routingArray) {
        super(urls);
        this.moduleId = moduleId;
        this.classLoaderService = classLoaderService;
        this.routingArray = routingArray;
    }

    public ModuleRoutingURLClassLoader(final String moduleId,
                                       final ClassLoaderService classLoaderService,
                                       final URL[] urls,
                                       final ClassLoader parent,
                                       final Routing... routingArray) {
        super(urls, parent);
        this.moduleId = moduleId;
        this.classLoaderService = classLoaderService;
        this.routingArray = routingArray;
    }

    /**
     * Find export resource
     *
     * @param resourceName 资源名称
     * @return 返回导出的资源url
     */
    protected URL getExportResource(String resourceName) {
        if (shouldFindExportedResource(resourceName)) {
            URL url;
            List<ClassLoaderFactory> exportResourceClassLoadersInOrder = classLoaderService
                    .findExportResourceClassLoadersInOrder(resourceName);
            if (exportResourceClassLoadersInOrder != null) {
                for (ClassLoaderFactory exportResourceClassLoaderFactory : exportResourceClassLoadersInOrder) {
                    ClassLoader classLoader = exportResourceClassLoaderFactory.getClassLoader(BizClassLoaderHolder.getBizClassLoader());
                    url = classLoader.getResource(resourceName);
                    if (url != null) {
                        return url;
                    }
                }
            }

        }
        return null;
    }

    private String transformClassName(String name) {
        if (name.endsWith(CLASS_RESOURCE_SUFFIX)) {
            name = name.substring(0, name.length() - CLASS_RESOURCE_SUFFIX.length());
        }
        return name.replace("/", ".");
    }

    /**
     * Find .class resource
     *
     * @param resourceName 资源名称
     * @return 返回类资源URL
     */
    protected URL getClassResource(String resourceName) {
        if (resourceName.endsWith(CLASS_RESOURCE_SUFFIX)) {
            String className = transformClassName(resourceName);
            if (shouldFindExportedClass(className)) {
                ClassLoaderFactory classLoaderFactory = classLoaderService.findExportClassLoaderFactory(className);
                if (classLoaderFactory == null) {
                    return null;
                }
                ClassLoader classLoader = classLoaderFactory.getClassLoader(BizClassLoaderHolder.getBizClassLoader());
                return classLoader == null ? null : classLoader.getResource(resourceName);
            }
        }
        return null;
    }

    protected URL getBusinessResource(String name) {
        if (BizClassLoaderHolder.getBizClassLoader() != null) {
            return BizClassLoaderHolder.getBizClassLoader().getResource(name);
        }
        return null;
    }

    protected Enumeration<URL> getBusinessResources(String name) throws IOException {
        if (BizClassLoaderHolder.getBizClassLoader() == null) {
            return BizClassLoaderHolder.getBizClassLoader().getResources(name);
        }
        return EmptyEnumeration.emptyEnumeration();
    }

    /**
     * Real logic to get resource
     *
     * @param name 资源名称
     * @return 返回资源URL
     */
    @Override
    protected URL getResourceInternal(String name) {
        // 1. find routing resource
        URL url = getRoutingResource(name);

        // 2. find jdk resource
        if (url == null) {
            url = getJdkResource(name);
        }

        // 3. find export resource
        if (url == null) {
            url = getExportResource(name);
        }

        // 4. get local resource
        if (url == null) {
            url = getLocalResource(name);
        }
        // 5. get .class resource
        if (url == null) {
            url = getClassResource(name);
        }

        // 6. find business classloader resource
        if (url == null) {
            url = getBusinessResource(name);
        }

        return url;
    }

    /**
     * Find export resources
     *
     * @param resourceName 资源名称
     * @return 返回资源的URL集合
     * @throws IOException 读取资源错误可能会抛出IOException
     */
    @SuppressWarnings("unchecked")
    protected Enumeration<URL> getExportResources(String resourceName) throws IOException {
        if (shouldFindExportedResource(resourceName)) {
            List<ClassLoaderFactory> exportResourceClassLoadersInOrder = classLoaderService
                    .findExportResourceClassLoadersInOrder(resourceName);
            if (exportResourceClassLoadersInOrder != null) {
                List<Enumeration<URL>> enumerationList = new ArrayList<Enumeration<URL>>();
                for (ClassLoaderFactory exportResourceClassLoaderFactory : exportResourceClassLoadersInOrder) {
                    ClassLoader classLoader = exportResourceClassLoaderFactory.getClassLoader(BizClassLoaderHolder.getBizClassLoader());
                    enumerationList.add(((ModuleRoutingURLClassLoader) classLoader)
                            .getLocalResources(resourceName));
                }
                return new CompoundEnumeration<URL>(
                        enumerationList.toArray((Enumeration<URL>[]) new Enumeration<?>[0]));
            }
        }
        return EmptyEnumeration.emptyEnumeration();
    }

    /**
     * Real logic to get resources
     *
     * @param name 资源名称
     * @return 返回资源列表
     * @throws IOException 资源加载不到会抛出IOException
     */
    @Override
    protected Enumeration<URL> getResourcesInternal(String name) throws IOException {
        List<Enumeration<URL>> enumerationList = new ArrayList<Enumeration<URL>>();
        // 1. find routing resources
        enumerationList.add(getRoutingResources(name));
        // 2. find jdk resources
        enumerationList.add(getJdkResources(name));

        // 3. find exported resources
        enumerationList.add(getExportResources(name));

        // 4. find local resources
        enumerationList.add(getLocalResources(name));

        // 5. find business classloader resources
        enumerationList.add(getBusinessResources(name));


        return new CompoundEnumeration<URL>(
                enumerationList.toArray((Enumeration<URL>[]) new Enumeration<?>[0]));
    }

    /**
     * Whether to find class that exported by other classloader
     *
     * @param className class name
     * @return 返回是否需要查找导到的类
     */
    abstract boolean shouldFindExportedClass(String className);

    /**
     * Whether to find resource that exported by other classloader
     *
     * @param resourceName 资源名称
     * @return 返回是否需要查找导出的资源
     */
    abstract boolean shouldFindExportedResource(String resourceName);

    /**
     * 从 Routing 中加载
     *
     * @param javaClassName
     * @param resolve
     * @return
     */
    @Override
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
     * Load export class
     *
     * @param name 类名
     * @return 返回Class
     */
    protected Class<?> resolveExportClass(String name) {
        if (shouldFindExportedClass(name)) {
            ClassLoaderFactory importClassLoaderFactory = classLoaderService.findExportClassLoaderFactory(name);
            if (importClassLoaderFactory != null) {
                try {
                    ClassLoader classLoader = importClassLoaderFactory.getClassLoader(BizClassLoaderHolder.getBizClassLoader());
                    return classLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    // just log when debug level
                    if (logger.isDebugEnabled()) {
                        // log debug message
                        logger.debug(
                                "SIMULATOR: Fail to load export class " + name, e);
                    }
                }
            }
        }
        return null;
    }

    protected Class<?> loadClassInternal(String name, boolean resolve)
            throws ClassNotFoundException {
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

        // 4. Import class export by other plugins
        if (clazz == null) {
            clazz = resolveExportClass(name);
        }

        // 5. module classpath class
        if (clazz == null) {
            clazz = resolveLocalClass(name);
        }

        // 6. load class from business classloader
        if (clazz == null) {
            clazz = resolveBusinessClassLoader(name);
        }

        // 7. load class from super
        if (clazz == null) {
            clazz = resolveSystemClass(name, resolve);
        }

        if (clazz != null) {
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }

        throw new ClassNotFoundException("class " + name + " not found in module: " + moduleId);
    }

    /**
     * load class from business classloader
     *
     * @param name classname
     * @return true/false
     */
    protected Class resolveBusinessClassLoader(String name) {
        try {
            if (BizClassLoaderHolder.getBizClassLoader() == null) {
                return null;
            }
            return BizClassLoaderHolder.getBizClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
        } catch (NoClassDefFoundError e) {
        }
        return null;
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        return classLoadingLock.loadingInLock(name, new ClassLoadingLock.ClassLoading() {
            @Override
            public Class<?> loadClass(String javaClassName) throws ClassNotFoundException {
                definePackageIfNecessary(name);
                return loadClassInternal(name, resolve);
            }
        });
    }

    /**
     * Define a package before a {@code findClass} call is made. This is necessary to
     * ensure that the appropriate manifest for nested JARs is associated with the
     * package.
     *
     * @param className the class name being found
     */
    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (getPackage(packageName) == null) {
                try {
                    definePackage(className, packageName);
                } catch (IllegalArgumentException ex) {
                    // Tolerate race condition due to being parallel capable
                }
            }
        }
    }

    private void definePackage(final String className, final String packageName) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                @Override
                public Object run() throws Exception {
                    StringBuilder pen = new StringBuilder(packageName.length() + 10);
                    StringBuilder cen = new StringBuilder(className.length() + 10);
                    String packageEntryName = pen.append(packageName.replace('.', '/')).append("/")
                            .toString();
                    String classEntryName = cen.append(className.replace('.', '/'))
                            .append(".class").toString();
                    for (URL url : getURLs()) {
                        try {
                            URLConnection connection = url.openConnection();
                            if (connection instanceof JarURLConnection) {
                                JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                                if (jarFile.getEntry(classEntryName) != null
                                        && jarFile.getEntry(packageEntryName) != null
                                        && jarFile.getManifest() != null) {
                                    definePackage(packageName, jarFile.getManifest(), url);
                                    return null;
                                }
                            }
                        } catch (IOException ex) {
                            // Ignore
                        }
                    }
                    return null;
                }
            }, AccessController.getContext());
        } catch (java.security.PrivilegedActionException ex) {
            // Ignore
        }
    }

}
