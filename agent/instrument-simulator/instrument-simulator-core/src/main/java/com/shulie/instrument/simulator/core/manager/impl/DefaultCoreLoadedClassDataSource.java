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

import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.util.ArrayUtils;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.core.manager.CoreLoadedClassDataSource;
import com.shulie.instrument.simulator.core.util.SimulatorClassUtils;
import com.shulie.instrument.simulator.api.guard.SimulatorGuard;
import com.shulie.instrument.simulator.core.util.SimulatorStringUtils;
import com.shulie.instrument.simulator.core.util.matcher.ExtFilterMatcher;
import com.shulie.instrument.simulator.core.util.matcher.Matcher;
import com.shulie.instrument.simulator.core.util.matcher.UnsupportedMatcher;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructureFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.*;

import static com.shulie.instrument.simulator.api.filter.ExtFilterFactory.make;

/**
 * 已加载类数据源默认实现
 */
public class DefaultCoreLoadedClassDataSource implements CoreLoadedClassDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Instrumentation inst;
    private final boolean isEnableUnsafe;

    public DefaultCoreLoadedClassDataSource(final Instrumentation inst,
                                            final boolean isEnableUnsafe) {
        this.inst = inst;
        this.isEnableUnsafe = isEnableUnsafe;
    }

    @Override
    public Set<Class<?>> list() {
        final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            classes.add(clazz);
        }
        return classes;
    }

    @Override
    public Iterator<Class<?>> iteratorForLoadedClasses() {
        return new Iterator<Class<?>>() {

            final Class<?>[] loaded = inst.getAllLoadedClasses();
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < loaded.length;
            }

            @Override
            public Class<?> next() {
                return loaded[pos++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public List<Class<?>> findForReTransform(final Matcher matcher) {
        return find(matcher, true);
    }

    @Override
    public List<Class<?>> findForReTransform(String className) {
        SimulatorGuard.getInstance().enter();
        try {

            final List<Class<?>> classes = new ArrayList<Class<?>>();
            if (null == className) {
                return classes;
            }

            final Iterator<Class<?>> itForLoaded = iteratorForLoadedClasses();
            while (itForLoaded.hasNext()) {
                final Class<?> clazz = itForLoaded.next();

                // 过滤掉Simulator家族的类
                if (SimulatorClassUtils.isComeFromSimulatorFamily(SimulatorStringUtils.toInternalClassName(clazz.getName()), clazz.getClassLoader())) {
                    continue;
                }

                try {
                    if (clazz.getName().equals(className)) {
                        classes.add(clazz);
                    }
                } catch (Throwable cause) {
                    // 当解析类出现异常的时候，直接认为根本没有这个类即可
                    logger.warn("SIMULATOR: remove from findForReTransform, because loading class:{} occur an exception", clazz.getName(), cause);
                }
            }
            return classes;

        } finally {
            SimulatorGuard.getInstance().exit();
        }
    }

    @Override
    public List<Class<?>> findForReTransform(String... classNames) {
        if (ArrayUtils.isEmpty(classNames)) {
            return Collections.EMPTY_LIST;
        }
        SimulatorGuard.getInstance().enter();
        try {
            final List<Class<?>> classes = new ArrayList<Class<?>>();
            for (String className : classNames) {
                if (null == className) {
                    return classes;
                }

                final Iterator<Class<?>> itForLoaded = iteratorForLoadedClasses();
                while (itForLoaded.hasNext()) {
                    final Class<?> clazz = itForLoaded.next();

                    // 过滤掉Simulator家族的类
                    if (SimulatorClassUtils.isComeFromSimulatorFamily(SimulatorStringUtils.toInternalClassName(clazz.getName()), clazz.getClassLoader())) {
                        continue;
                    }

                    try {
                        if (clazz.getName().equals(className)) {
                            classes.add(clazz);
                        }
                    } catch (Throwable cause) {
                        // 当解析类出现异常的时候，直接认为根本没有这个类即可
                        logger.warn("SIMULATOR: remove from findForReTransform, because loading class:{} occur an exception", clazz.getName(), cause);
                    }
                }
            }
            return classes;

        } finally {
            SimulatorGuard.getInstance().exit();
        }
    }

    @Override
    public List<Class<?>> findForReTransform(Collection<String> classNames) {
        if (CollectionUtils.isEmpty(classNames)) {
            return Collections.EMPTY_LIST;
        }
        SimulatorGuard.getInstance().enter();
        try {
            final List<Class<?>> classes = new ArrayList<Class<?>>();
            for (String className : classNames) {
                if (null == className) {
                    return classes;
                }

                final Iterator<Class<?>> itForLoaded = iteratorForLoadedClasses();
                while (itForLoaded.hasNext()) {
                    final Class<?> clazz = itForLoaded.next();

                    // 过滤掉Simulator家族的类
                    if (SimulatorClassUtils.isComeFromSimulatorFamily(SimulatorStringUtils.toInternalClassName(clazz.getName()), clazz.getClassLoader())) {
                        continue;
                    }

                    try {
                        if (clazz.getName().equals(className)) {
                            classes.add(clazz);
                        }
                    } catch (Throwable cause) {
                        // 当解析类出现异常的时候，直接认为根本没有这个类即可
                        logger.warn("SIMULATOR: remove from findForReTransform, because loading class:{} occur an exception", clazz.getName(), cause);
                    }
                }
            }
            return classes;

        } finally {
            SimulatorGuard.getInstance().exit();
        }
    }

    private List<Class<?>> find(final Matcher matcher,
                                final boolean isRemoveUnsupported) {

        SimulatorGuard.getInstance().enter();
        try {

            final List<Class<?>> classes = new ArrayList<Class<?>>();
            if (null == matcher) {
                return classes;
            }

            final Iterator<Class<?>> itForLoaded = iteratorForLoadedClasses();
            while (itForLoaded.hasNext()) {
                final Class<?> clazz = itForLoaded.next();

                // 过滤掉Simulator家族的类
                if (SimulatorClassUtils.isComeFromSimulatorFamily(SimulatorStringUtils.toInternalClassName(clazz.getName()), clazz.getClassLoader())) {
                    continue;
                }

                // 过滤掉对于JVM认为不可修改的类
                if (isRemoveUnsupported
                        && !inst.isModifiableClass(clazz)) {
                    logger.debug("SIMULATOR: remove from findForReTransform, because class:{} is unModifiable", clazz.getName());
                    continue;
                }
                try {
                    if (isRemoveUnsupported) {
                        if (isMatchedUnsupported(matcher, clazz)) {
                            classes.add(clazz);
                        }
                    } else {
                        if (isMatched(matcher, clazz)) {
                            classes.add(clazz);
                        }
                    }

                } catch (Throwable cause) {
                    // 当解析类出现异常的时候，直接认为根本没有这个类即可
                    logger.warn("SIMULATOR: remove from findForReTransform, because loading class:{} occur an exception", clazz.getName(), cause);
                }
            }
            return classes;

        } finally {
            SimulatorGuard.getInstance().exit();
        }

    }

    /**
     * 这个地方匹配在加载类时有可能因为目标类依赖的其他类不存在而导致获取构造函数或者方法会
     * 抛出 ClassNotFoundException 或者是 NoClassDefError
     * 所以如果出现错误则默认返回匹配不上即可
     *
     * @param matcher 匹配器
     * @param clazz   目标类
     * @return 返回匹配成功或失败
     */
    private boolean isMatched(Matcher matcher, Class<?> clazz) {
        try {
            return matcher.matching(ClassStructureFactory.createClassStructure(clazz)).isMatched();
        } catch (Throwable e) {
            logger.warn("SIMULATOR: clazz {} matches occur err, matches false. {}", clazz.getName(), e.getMessage());
            return false;
        }
    }

    /**
     * 这个地方匹配在加载类时有可能因为目标类依赖的其他类不存在而导致获取构造函数或者方法会
     * 抛出 ClassNotFoundException 或者是 NoClassDefError
     * 所以如果出现错误则默认返回匹配不上即可
     *
     * @param matcher 匹配器
     * @param clazz   目标类
     * @return 返回匹配成功或失败
     */
    private boolean isMatchedUnsupported(Matcher matcher, Class<?> clazz) {
        try {
            return isMatched(new UnsupportedMatcher(clazz.getClassLoader(), isEnableUnsafe)
                    .and(matcher), clazz);
        } catch (Throwable e) {
            logger.warn("SIMULATOR: clazz {} matches occur err, matches false. {}", clazz.getName(), e.getMessage());
            return false;
        }
    }


    /**
     * 根据过滤器搜索出匹配的类集合
     *
     * @param filter 扩展过滤器
     * @return 匹配的类集合
     */
    @Override
    public Set<Class<?>> find(Filter filter) {
        return new LinkedHashSet<Class<?>>(find(new ExtFilterMatcher(make(filter)), false));
    }

}
