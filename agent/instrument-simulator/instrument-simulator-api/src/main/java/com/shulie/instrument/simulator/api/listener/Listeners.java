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
package com.shulie.instrument.simulator.api.listener;

import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;

import java.util.Arrays;

/**
 * 监听器构建类,构建监听器时使用
 * 此类为了减少默认模块类加载器更少加载模块中的类而设计，通常此种情形存在于多业务类加载器实例的
 * 情况下，此种情况下默认模块类加载器通常只是加载 Module 的声明类，这种情况下可以最大化的减少默认
 * 模块类加载器加载模块中的类，因为这不是必须的
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/21 5:25 下午
 */
public class Listeners {

    public static final Listeners NONE = new Listeners();

    private String className;
    private Object[] args;

    /**
     * 执行策略
     */
    private ExecutionPolicy executionPolicy = ExecutionPolicy.ALWAYS;

    /**
     * 作用域名称
     */
    private String scopeName;

    /**
     * scope作用域的回调
     */
    private AdviceListenerCallback adviceListenerCallback;

    /**
     * scope 作用域的回调
     */
    private EventListenerCallback eventListenerCallback;

    private Listeners() {
    }

    public static Listeners of(final String className, final String scopeName, final ExecutionPolicy policy, AdviceListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(className);
        listeners.setScopeName(scopeName);
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setAdviceListenerCallback(callback);
        return listeners;
    }

    public static Listeners of(final String className, final String scopeName, final ExecutionPolicy policy, EventListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(className);
        listeners.setScopeName(scopeName);
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setEventListenerCallback(callback);
        return listeners;
    }

    public static Listeners of(final Class clazz, final Object... args) {
        return of(clazz, null, ExecutionPolicy.ALWAYS, (AdviceListenerCallback) null, args);
    }

    public static Listeners of(final Class clazz, final String scopeName, final EventListenerCallback callback, final Object... args) {
        return of(clazz, scopeName, ExecutionPolicy.ALWAYS, callback, args);
    }

    public static Listeners dynamicScope(final Class clazz, final EventListenerCallback callback, final Object... args) {
        return dynamicScope(clazz, ExecutionPolicy.ALWAYS, callback, args);
    }

    public static Listeners of(final Class clazz, final String scopeName, final AdviceListenerCallback callback, final Object... args) {
        return of(clazz, scopeName, ExecutionPolicy.ALWAYS, callback, args);
    }

    public static Listeners dynamicScope(final Class clazz, final AdviceListenerCallback callback, final Object... args) {
        return dynamicScope(clazz, ExecutionPolicy.ALWAYS, callback, args);
    }

    public static Listeners of(final Class clazz, final String scopeName, final ExecutionPolicy policy, final EventListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(clazz.getName());
        listeners.setScopeName(scopeName);
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setEventListenerCallback(callback);
        return listeners;
    }

    public static Listeners dynamicScope(final Class clazz, final ExecutionPolicy policy, final EventListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(clazz.getName());
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setEventListenerCallback(callback);
        return listeners;
    }

    public static Listeners of(final Class clazz, final String scopeName, final ExecutionPolicy policy, final AdviceListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(clazz.getName());
        listeners.setScopeName(scopeName);
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setAdviceListenerCallback(callback);
        return listeners;
    }

    public static Listeners dynamicScope(final Class clazz, final ExecutionPolicy policy, final AdviceListenerCallback callback, final Object... args) {
        Listeners listeners = new Listeners();
        listeners.setClassName(clazz.getName());
        listeners.setArgs(args);
        listeners.setExecutionPolicy(policy);
        listeners.setAdviceListenerCallback(callback);
        return listeners;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setExecutionPolicy(ExecutionPolicy executionPolicy) {
        this.executionPolicy = executionPolicy;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getClassName() {
        return className;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getScopeName() {
        return scopeName;
    }

    public AdviceListenerCallback getAdviceListenerCallback() {
        return adviceListenerCallback;
    }

    public void setAdviceListenerCallback(AdviceListenerCallback adviceListenerCallback) {
        this.adviceListenerCallback = adviceListenerCallback;
    }

    public EventListenerCallback getEventListenerCallback() {
        return eventListenerCallback;
    }

    public void setEventListenerCallback(EventListenerCallback eventListenerCallback) {
        this.eventListenerCallback = eventListenerCallback;
    }

    public ExecutionPolicy getExecutionPolicy() {
        return executionPolicy;
    }

    @Override
    public String toString() {
        return "Listeners{" +
                "className='" + className + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Listeners listeners = (Listeners) o;

        if (className != null ? !className.equals(listeners.className) : listeners.className != null) {
            return false;
        }
        if (executionPolicy != listeners.executionPolicy) {
            return false;
        }
        return scopeName != null ? scopeName.equals(listeners.scopeName) : listeners.scopeName == null;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (executionPolicy != null ? executionPolicy.hashCode() : 0);
        result = 31 * result + (scopeName != null ? scopeName.hashCode() : 0);
        return result;
    }
}
