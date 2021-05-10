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

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ModuleRuntimeException;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.resource.ModuleCommandInvoker;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.exception.SimulatorException;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 3:30 下午
 */
class DefaultModuleCommandInvoker implements ModuleCommandInvoker {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SLASH = "/";
    private CoreModuleManager coreModuleManager;

    public DefaultModuleCommandInvoker(CoreModuleManager coreModuleManager) {
        this.coreModuleManager = coreModuleManager;
    }

    @Override
    public <T> CommandResponse<T> invokeCommand(String uniqueId, String command) {
        return invokeCommand(uniqueId, command, Collections.EMPTY_MAP);
    }

    @Override
    public <T> CommandResponse<T> invokeCommand(final String uniqueId, String command, Map<String, String> args) {
        final CoreModule coreModule = coreModuleManager.get(uniqueId);
        if (coreModule == null) {
            throw new ModuleRuntimeException(uniqueId, ModuleRuntimeException.ErrorCode.MODULE_NOT_EXISTED);
        }
        // 匹配对应的方法
        final Method method = matchingModuleMethod(
                command,
                coreModule.getModule().getClass()
        );

        if (method == null) {
            throw new ModuleRuntimeException(uniqueId, ModuleRuntimeException.ErrorCode.MODULE_COMMAND_NOT_EXISTED);
        }

        // 自动释放I/O资源
        final List<Closeable> autoCloseResources = coreModule.append(new ReleaseResource<List<Closeable>>(new ArrayList<Closeable>()) {
            @Override
            public void release() {
                final List<Closeable> closeables = get();
                if (CollectionUtils.isEmpty(closeables)) {
                    return;
                }
                for (final Closeable closeable : get()) {
                    if (closeable instanceof Flushable) {
                        try {
                            ((Flushable) closeable).flush();
                        } catch (Exception cause) {
                            logger.warn("SIMULATOR: uniqueId={} flush I/O occur error!", uniqueId, cause);
                        }
                    }
                    try {
                        closeable.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        // 生成方法调用参数
        final Object[] parameterObjectArray = generateParameterObjectArray(method, args);
        final boolean isAccessible = method.isAccessible();
        final ClassLoader oriThreadContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            method.setAccessible(true);
            Thread.currentThread().setContextClassLoader(coreModule.getClassLoaderFactory().getDefaultClassLoader());
            Object value = method.invoke(coreModule.getModule(), parameterObjectArray);
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: invoke module {} method {} success.", uniqueId, method.getName());
            }
            return (CommandResponse) value;
        } catch (IllegalAccessException iae) {
            logger.warn("SIMULATOR: invoke module {} method {} occur access denied.", uniqueId, method.getName(), iae);
            throw new SimulatorException(iae);
        } catch (InvocationTargetException ite) {
            logger.warn("SIMULATOR: invoke module {} method {} occur error.", uniqueId, method.getName(), ite.getTargetException());
            final Throwable targetCause = ite.getTargetException();
            throw new SimulatorException(targetCause);
        } finally {
            Thread.currentThread().setContextClassLoader(oriThreadContextClassLoader);
            method.setAccessible(isAccessible);
            coreModule.release(autoCloseResources);
        }
    }

    /**
     * 生成方法请求参数数组
     * 主要用于填充HttpServletRequest和HttpServletResponse
     *
     * @param method 模块Java方法
     * @return 请求方法参数列表
     */
    private Object[] generateParameterObjectArray(final Method method,
                                                  final Map<String, String> args) {

        final Class<?>[] parameterTypeArray = method.getParameterTypes();
        if (ArrayUtils.isEmpty(parameterTypeArray)) {
            return null;
        }
        final Object[] parameterObjectArray = new Object[parameterTypeArray.length];
        for (int index = 0; index < parameterObjectArray.length; index++) {
            final Class<?> parameterType = parameterTypeArray[index];

            // ParameterMap<String,String>
            if (Map.class.isAssignableFrom(parameterType)
                    && isMapWithGenericParameterTypes(method, index, String.class, String.class)) {
                parameterObjectArray[index] = (args == null ? Collections.EMPTY_MAP : args);
            }
        }

        return parameterObjectArray;
    }

    private boolean isMapWithGenericParameterTypes(final Method method,
                                                   final int parameterIndex,
                                                   final Class<?> keyClass,
                                                   final Class<?> valueClass) {
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length < parameterIndex
                || !(genericParameterTypes[parameterIndex] instanceof ParameterizedType)) {
            return false;
        }
        final Type[] actualTypeArguments = ((ParameterizedType) genericParameterTypes[parameterIndex]).getActualTypeArguments();
        return actualTypeArguments.length == 2
                && keyClass.equals(actualTypeArguments[0])
                && valueClass.equals(actualTypeArguments[1]);
    }

    /**
     * 匹配模块中复合HTTP请求路径的方法
     * 匹配方法的方式是：HttpMethod和HttpPath全匹配
     *
     * @param command       命令
     * @param classOfModule 模块类
     * @return 返回匹配上的方法，如果没有找到匹配方法则返回null
     */
    private Method matchingModuleMethod(final String command,
                                        final Class<?> classOfModule) {

        // 查找@Command注解的方法
        for (final Method method : getMethodsListWithAnnotation(classOfModule, Command.class)) {
            final Command commandAnnotation = method.getAnnotation(Command.class);
            if (null == commandAnnotation) {
                continue;
            }
            // 兼容 value 是否以 / 开头的写法
            String cmd = trimSlash(commandAnnotation.value());
            if (StringUtils.equals(cmd, command)) {
                return method;
            }
        }
        // 找不到匹配方法，返回null
        return null;
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getMethodsListWithAnnotation(cls, annotationCls, false, false);
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls,
                                                             final Class<? extends Annotation> annotationCls,
                                                             boolean searchSupers, boolean ignoreAccess) {

        List<Class<?>> classes = (searchSupers ? getAllSuperclassesAndInterfaces(cls)
                : new ArrayList<Class<?>>());
        classes.add(0, cls);
        final List<Method> annotatedMethods = new ArrayList<Method>();
        for (Class<?> acls : classes) {
            final Method[] methods = (ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods());
            for (final Method method : methods) {
                if (method.getAnnotation(annotationCls) != null) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<Class<?>>();
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
        int superClassIndex = 0;
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() ||
                superClassIndex < allSuperclasses.size()) {
            Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if (superClassIndex >= allSuperclasses.size()) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (interfaceIndex < superClassIndex) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (superClassIndex < interfaceIndex) {
                acls = allSuperclasses.get(superClassIndex++);
            } else {
                acls = allInterfaces.get(interfaceIndex++);
            }
            allSuperClassesAndInterfaces.add(acls);
        }
        return allSuperClassesAndInterfaces;
    }

    private String trimSlash(String cmd) {
        // 若不以 / 开头，则添加 /
        if (cmd.startsWith(SLASH)) {
            return cmd.substring(1);
        }
        return cmd;
    }
}
