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

import com.shulie.instrument.simulator.api.annotation.Interrupted;
import com.shulie.instrument.simulator.api.listener.Interruptable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectUtils {

    /**
     * 获取Java类的方法
     * 该方法不会抛出任何声明式异常
     *
     * @param clazz               类
     * @param name                方法名
     * @param parameterClassArray 参数类型数组
     * @return Java方法
     */
    public static Method getDeclaredJavaMethodUnCaught(final Class<?> clazz,
                                                       final String name,
                                                       final Class<?>... parameterClassArray) {
        try {
            return clazz.getDeclaredMethod(name, parameterClassArray);
        } catch (NoSuchMethodException e) {
            throw new UnCaughtException(e);
        }
    }

    /**
     * 获取Java类的构造函数
     * 该方法不会抛出任何声明式异常
     *
     * @param clazz               类
     * @param parameterClassArray 参数类型数组
     * @return Java方法
     */
    public static Constructor getDeclaredJavaConstructorUnCaught(final Class<?> clazz,
                                                                 final Class<?>... parameterClassArray) {
        try {
            return clazz.getDeclaredConstructor(parameterClassArray);
        } catch (NoSuchMethodException e) {
            throw new UnCaughtException(e);
        }
    }

    /**
     * 通过反射调用目标方法，如果出现错误会抛出 {@link UnCaughtException}
     *
     * @param method         目标方法
     * @param target         目标对象
     * @param parameterArray 方法入参
     * @param <T>            返回类型的泛型，可能会出现强转错误
     * @return
     */
    public static <T> T invokeMethodUnCaught(final Method method,
                                             final Object target,
                                             final Object... parameterArray) {
        final boolean isAccessible = method.isAccessible();
        try {
            method.setAccessible(true);
            return (T) method.invoke(target, parameterArray);
        } catch (Throwable e) {
            throw new UnCaughtException(e);
        } finally {
            method.setAccessible(isAccessible);
        }
    }

    /**
     * 获取目标 Class 对应的属性 Field
     *
     * @param clazz 目标 Class
     * @param name  属性名称
     * @return
     */
    public static Field getDeclaredJavaFieldUnCaught(final Class<?> clazz,
                                                     final String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new UnCaughtException(e);
        }
    }

    /**
     * 通过反射获取属性值，如果出现错误会抛出{@link UnCaughtException}
     *
     * @param clazz  目标 Class
     * @param name   需要设置的属性名称
     * @param target 目标对象
     * @param <T>    返回的泛型, 可能会导致强转错误
     * @return
     */
    public static <T> T getDeclaredJavaFieldValueUnCaught(final Class<?> clazz,
                                                          final String name,
                                                          final Object target) {
        final Field field = getDeclaredJavaFieldUnCaught(clazz, name);
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (IllegalAccessException e) {
            throw new UnCaughtException(e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * 通过反射设置属性值，如果出现错误会抛出 {@link UnCaughtException}
     *
     * @param clazz  目标 Class
     * @param name   需要设置的属性名称
     * @param target 目标对象
     * @param value  需要设置的属性值
     */
    public static void setDeclaredJavaFieldValueUnCaught(final Class<?> clazz,
                                                         final String name,
                                                         final Object target,
                                                         final Object value) {
        final Field field = getDeclaredJavaFieldUnCaught(clazz, name);
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new UnCaughtException(e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * 定义类
     *
     * @param loader         目标ClassLoader
     * @param javaClassName  类名称
     * @param classByteArray 类字节码数组
     * @return 定义的类
     * @throws InvocationTargetException 目标方法调用发生异常
     * @throws IllegalAccessException    目标方法不可进入
     */
    public static Class<?> defineClass(final ClassLoader loader,
                                       final String javaClassName,
                                       final byte[] classByteArray) throws InvocationTargetException, IllegalAccessException {

        final Method defineClassMethod =
                getDeclaredJavaMethodUnCaught(ClassLoader.class, "defineClass", String.class, byte[].class, int.class, int.class);

        synchronized (defineClassMethod) {
            final boolean acc = defineClassMethod.isAccessible();
            try {
                defineClassMethod.setAccessible(true);
                return (Class<?>) defineClassMethod.invoke(
                        loader,
                        javaClassName,
                        classByteArray,
                        0,
                        classByteArray.length
                );
            } finally {
                defineClassMethod.setAccessible(acc);
            }
        }

    }

    /**
     * 判断是否是中断式事件处理器
     *
     * @param eventListener 事件监听器
     * @return TRUE:中断式;FALSE:非中断式
     */
    public static boolean isInterruptEventHandler(final Object eventListener) {
        if (eventListener == null) {
            return false;
        }
        if (eventListener.getClass().isAnnotationPresent(Interrupted.class)) {
            return true;
        }
        if (eventListener instanceof Interruptable) {
            return ((Interruptable) eventListener).isInterrupted();
        }
        return false;
    }

    /**
     * 判断是否是中断式事件处理器
     *
     * @param clazz 目标类
     * @return TRUE:中断式;FALSE:非中断式
     */
    public static boolean isInterruptEventHandler(final Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isAnnotationPresent(Interrupted.class)) {
            return true;
        }
        return false;
    }
}
