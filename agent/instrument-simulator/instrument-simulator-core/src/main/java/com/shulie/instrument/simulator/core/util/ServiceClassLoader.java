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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/19 8:32 下午
 */
public class ServiceClassLoader implements Iterable<Class<?>> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceClassLoader.class);

    private static final String PREFIX = "META-INF/services/";

    /**
     * 需要加载实现的服务类接口
     */
    private final Class serviceClass;
    /**
     * 类加载器
     */
    private final ClassLoader classLoader;

    /**
     * 访问控制上下文
     */
    private final AccessControlContext acc;

    // Cached providers, in instantiation order
    private LinkedHashMap<String, Class<?>> providers = new LinkedHashMap<String, Class<?>>();

    // The current lazy-lookup iterator
    private LazyIterator lookupIterator;


    /**
     * 所有的构造全使用{@link #load(Class)}和{@link #load(Class, ClassLoader)}
     *
     * @param serviceClass 服务接口
     * @param classLoader  类加载器
     */
    private ServiceClassLoader(final Class serviceClass, ClassLoader classLoader) {
        this.serviceClass = serviceClass;
        this.classLoader = (classLoader == null) ? ClassLoader.getSystemClassLoader() : classLoader;
        this.acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
        reload();
    }

    public void reload() {
        providers.clear();
        lookupIterator = new LazyIterator(this.serviceClass, this.classLoader);
    }

    private static void fail(Class<?> service, String msg, Throwable cause)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
                cause);
    }

    private static void fail(Class<?> service, String msg)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class<?> service, URL u, int line, String msg)
            throws ServiceConfigurationError {
        fail(service, u + ":" + line + ": " + msg);
    }

    public static ServiceClassLoader load(Class service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ServiceClassLoader.load(service, cl);
    }

    public static ServiceClassLoader load(Class<?> service, ClassLoader classLoader) {
        return new ServiceClassLoader(service, classLoader);
    }

    private int parseLine(Class<?> service, URL u, BufferedReader r, int lc,
                          List<String> names)
            throws IOException, ServiceConfigurationError {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) {
            ln = ln.substring(0, ci);
        }
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
                fail(service, u, lc, "Illegal configuration-file syntax");
            }
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)) {
                fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
                }
            }
            if (!providers.containsKey(ln) && !names.contains(ln)) {
                names.add(ln);
            }
        }
        return lc + 1;
    }

    private Iterator<String> parse(Class<?> service, URL u)
            throws ServiceConfigurationError {
        InputStream in = null;
        BufferedReader r = null;
        List<String> names = new ArrayList<String>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names)) >= 0) {
            }
        } catch (IOException x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    private class LazyIterator
            implements Iterator<Class<?>> {

        Class<?> service;
        ClassLoader loader;
        Enumeration<URL> configs = null;
        Iterator<String> pending = null;
        String nextName = null;

        private LazyIterator(Class<?> service, ClassLoader loader) {
            this.service = service;
            this.loader = loader;
        }

        private boolean hasNextService() {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + service.getName();
                    if (loader == null) {
                        configs = ClassLoader.getSystemResources(fullName);
                    } else {
                        configs = loader.getResources(fullName);
                    }
                } catch (IOException x) {
                    fail(service, "Error locating configuration files", x);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(service, configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }

        private Class<?> nextService() {
            if (!hasNextService()) {
                throw new NoSuchElementException();
            }
            String cn = nextName;
            nextName = null;
            Class<?> c = null;
            try {
                c = Class.forName(cn, false, loader);
            } catch (ClassNotFoundException x) {
                fail(service,
                        "Provider " + cn + " not found");
            }
            if (!service.isAssignableFrom(c)) {
                fail(service,
                        "Provider " + cn + " not a subtype");
            }
            try {

                providers.put(cn, c);
                return c;
            } catch (Throwable x) {
                fail(service,
                        "Provider " + cn + " could not be instantiated",
                        x);
            }
            throw new Error();          // This cannot happen
        }

        @Override
        public boolean hasNext() {
            if (acc == null) {
                return hasNextService();
            } else {
                PrivilegedAction<Boolean> action = new PrivilegedAction<Boolean>() {
                    @Override
                    public Boolean run() {
                        return hasNextService();
                    }
                };
                return AccessController.doPrivileged(action, acc);
            }
        }

        @Override
        public Class<?> next() {
            if (acc == null) {
                return nextService();
            } else {
                PrivilegedAction<Class<?>> action = new PrivilegedAction<Class<?>>() {
                    @Override
                    public Class<?> run() {
                        return nextService();
                    }
                };
                return AccessController.doPrivileged(action, acc);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public Iterator<Class<?>> iterator() {
        return new Iterator<Class<?>>() {

            Iterator<Map.Entry<String, Class<?>>> knownProviders
                    = providers.entrySet().iterator();

            @Override
            public boolean hasNext() {
                if (knownProviders.hasNext()) {
                    return true;
                }
                return lookupIterator.hasNext();
            }

            @Override
            public Class<?> next() {
                if (knownProviders.hasNext()) {
                    return knownProviders.next().getValue();
                }
                return lookupIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }
}
