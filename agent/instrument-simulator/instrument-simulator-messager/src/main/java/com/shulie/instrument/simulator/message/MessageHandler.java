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
package com.shulie.instrument.simulator.message;

/**
 * 信使处理器，一个 session 会对应一个 MessageHandler 的实例
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface MessageHandler {

    /**
     * 处理调用方法:执行之前
     * <p>CALL-BEFORE</p>
     *
     * @param listenerId  事件监听器ID
     * @param clazz       所在的目标类
     * @param isInterface 是否是接口
     * @param lineNumber  发生调用方法的代码行号
     * @param owner       调用方法的声明类
     * @param name        调用方法的方法名
     * @param desc        调用方法的方法描述
     * @throws Throwable 处理${调用方法:执行之前}失败
     */
    void handleOnCallBefore(int listenerId, Class clazz, boolean isInterface, int lineNumber, String owner, String name, String desc) throws Throwable;

    /**
     * 处理调用方法:正常返回
     * <p>CALL-RETURN</p>
     *
     * @param listenerId  事件监听器ID
     * @param clazz       所在的目标类
     * @param isInterface 是否是接口
     * @throws Throwable 处理{调用方法:正常返回}失败
     */
    void handleOnCallReturn(int listenerId, Class clazz, boolean isInterface) throws Throwable;

    /**
     * 处理调用方法:异常返回
     * <p>CALL-THROWS</p>
     *
     * @param listenerId  事件监听器ID
     * @param clazz       所在的目标类
     * @param isInterface 是否是接口
     * @param e           异常返回的异常
     * @throws Throwable 处理{调用方法:异常返回}失败
     */
    void handleOnCallThrows(int listenerId, Class clazz, boolean isInterface, Throwable e) throws Throwable;

    /**
     * 处理执行代码执行
     * <p>LINE</p>
     *
     * @param listenerId 事件监听器ID
     * @param clazz      所在的目标类
     * @param lineNumber 代码执行行号
     * @throws Throwable 处理代码执行行失败
     */
    void handleOnLine(int listenerId, Class clazz, int lineNumber) throws Throwable;

    /**
     * 处理方法调用:调用之前
     * <p>BEFORE</p>
     *
     * @param listenerId     事件监听器ID
     * @param argumentArray  参数数组
     * @param clazz          类名
     * @param javaMethodName 方法名
     * @param javaMethodDesc 方法签名
     * @param target         目标对象实例
     * @return Messager流程控制结果
     * @throws Throwable 处理{方法调用:调用之前}失败
     */
    Result handleOnBefore(int listenerId, Object[] argumentArray, Class clazz, String javaMethodName, String javaMethodDesc, Object target) throws Throwable;

    /**
     * 处理方法调用:异常返回
     *
     * @param listenerId 事件监听器ID
     * @param clazz      所在的目标类
     * @param throwable  异常返回的异常实例
     * @return Messager流程控制结果
     * @throws Throwable 处理{方法调用:异常返回}失败
     */
    Result handleOnThrows(int listenerId, Class clazz, Throwable throwable) throws Throwable;

    /**
     * 处理方法调用:正常返回
     *
     * @param listenerId 事件监听器ID
     * @param clazz      所在的目标类
     * @param object     正常返回的对象实例
     * @return Messager流程控制结果
     * @throws Throwable 处理{方法调用:正常返回}失败
     */
    Result handleOnReturn(int listenerId, Class clazz, Object object) throws Throwable;

}
