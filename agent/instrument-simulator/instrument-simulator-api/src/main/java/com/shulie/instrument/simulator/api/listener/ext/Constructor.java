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
 * 行为的构造器实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/5 4:59 下午
 */
public class Constructor implements Behavior {
    private final java.lang.reflect.Constructor<?> target;

    public Constructor(java.lang.reflect.Constructor<?> target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object obj, Object... args)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return target.newInstance(args);
    }

    @Override
    public boolean isAccessible() {
        return target.isAccessible();
    }

    @Override
    public void setAccessible(boolean accessFlag) {
        target.setAccessible(accessFlag);
    }

    @Override
    public String getName() {
        return "<init>";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return target.getParameterTypes();
    }

    @Override
    public Annotation[] getAnnotations() {
        return target.getAnnotations();
    }

    @Override
    public int getModifiers() {
        return target.getModifiers();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return target.getDeclaringClass();
    }

    @Override
    public Class<?> getReturnType() {
        return target.getDeclaringClass();
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        return target.getExceptionTypes();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return target.getDeclaredAnnotations();
    }

    @Override
    public AccessibleObject getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return target.equals(obj);
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
