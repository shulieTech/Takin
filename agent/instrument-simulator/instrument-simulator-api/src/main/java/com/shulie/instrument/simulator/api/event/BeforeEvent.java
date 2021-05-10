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
package com.shulie.instrument.simulator.api.event;

/**
 * 方法调用BEFORE事件
 *
 * 注意: Event 的内容会在方法调用周期结束后进行清理，内部的数据将会全部清空
 * 这样做是为了内存回收的速度加快，所以如果需要在方法调用生命周期外引用 Event
 * 需要提前将内部的数据引用
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class BeforeEvent extends InvokeEvent {

    /**
     * 触发调用事件的ClassLoader
     */
    private ClassLoader javaClassLoader;

    /**
     * 获取触发调用事件的类名称
     */
    private Class clazz;

    /**
     * 获取触发调用事件的方法名称
     */
    private String javaMethodName;

    /**
     * 获取触发调用事件的方法签名
     */
    private String javaMethodDesc;

    /**
     * 获取触发调用事件的对象
     */
    private Object target;

    /**
     * 获取触发调用事件的方法参数
     */
    private Object[] argumentArray;

    /**
     * 构造调用BEFORE事件
     *
     * @param processId       调用过程ID
     * @param invokeId        调用ID
     * @param javaClassLoader 触发调用事件的ClassLoader
     * @param clazz           触发调用事件的类名称
     * @param javaMethodName  触发调用事件的方法名称
     * @param javaMethodDesc  触发调用事件的方法签名
     * @param target          触发调用事件的对象(静态方法为null)
     * @param argumentArray   触发调用事件的方法参数
     */
    public BeforeEvent(final int processId,
                       final int invokeId,
                       final ClassLoader javaClassLoader,
                       final Class clazz,
                       final String javaMethodName,
                       final String javaMethodDesc,
                       final Object target,
                       final Object[] argumentArray) {
        super(processId, invokeId, EventType.BEFORE);
        this.javaClassLoader = javaClassLoader;
        this.clazz = clazz;
        this.javaMethodName = javaMethodName;
        this.javaMethodDesc = javaMethodDesc;
        this.target = target;
        this.argumentArray = argumentArray;
    }

    /**
     * 改变方法入参
     *
     * @param index       方法入参编号(从0开始)
     * @param changeValue 改变的值
     * @return this
     */
    public BeforeEvent changeParameter(final int index,
                                       final Object changeValue) {
        argumentArray[index] = changeValue;
        return this;
    }

    public ClassLoader getJavaClassLoader() {
        return javaClassLoader;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getJavaMethodName() {
        return javaMethodName;
    }

    public String getJavaMethodDesc() {
        return javaMethodDesc;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArgumentArray() {
        return argumentArray;
    }

    @Override
    public void destroy() {
        super.destroy();
        javaClassLoader = null;
        clazz = null;
        javaMethodName = null;
        javaMethodDesc = null;
        target = null;
        argumentArray = null;
    }
}
