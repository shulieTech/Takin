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
package com.shulie.instrument.simulator.api.listener.ext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

/**
 * 类行为，主要用来封装构造函数cinit/init/method
 * 具体可以参考{@link java.lang.reflect.Method}和{@link java.lang.reflect.Constructor}
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface Behavior {

    /**
     * 执行行为
     *
     * @param obj
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    Object invoke(Object obj, Object... args)
            throws IllegalAccessException, InvocationTargetException, InstantiationException;

    /**
     * 返回行为是否可访问
     *
     * @return
     */
    boolean isAccessible();

    /**
     * 设置行为是否可访问
     *
     * @param accessFlag
     */
    void setAccessible(boolean accessFlag);

    /**
     * 获取行为的名称
     *
     * @return
     */
    String getName();

    /**
     * 获取行为的参数类型列表
     *
     * @return
     */
    Class<?>[] getParameterTypes();

    /**
     * 获取行为的注解
     *
     * @return
     */
    Annotation[] getAnnotations();

    /**
     * 获取行为的修饰符
     *
     * @return
     */
    int getModifiers();

    /**
     * 获取行为所在的 Class
     *
     * @return
     */
    Class<?> getDeclaringClass();

    /**
     * 获取行为声明的返回值类型
     *
     * @return
     */
    Class<?> getReturnType();

    /**
     * 获取行为声明的异常类型
     *
     * @return
     */
    Class<?>[] getExceptionTypes();

    /**
     * 获取行为声明的的注解
     *
     * @return
     */
    Annotation[] getDeclaredAnnotations();

    /**
     * 获取被封装的目标对象: Method/Constructor
     *
     * @return 目标对象
     */
    AccessibleObject getTarget();

}
