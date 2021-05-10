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

import com.shulie.instrument.simulator.message.exception.ExceptionHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 信使，藏匿在各个ClassLoader中
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class Messager {

    private static final ConcurrentHashMap<String, Action> actionMap = new ConcurrentHashMap<String, Action>();

    /**
     * 异常处理器
     */
    private static volatile ExceptionHandler exceptionHandler;

    /**
     * 控制Messager是否在发生异常时主动对外抛出
     * T:主动对外抛出，会中断方法
     * F:不对外抛出，只将异常信息打印出来
     */
    public static volatile boolean isMessagerThrows = false;

    private static final ConcurrentHashMap<String, MessageHandler> namespaceMessagerHandlerMap
            = new ConcurrentHashMap<String, MessageHandler>();

    /**
     * 注册异常处理器
     *
     * @param exceptionHandler
     */
    public static void registerExceptionHandler(ExceptionHandler exceptionHandler) {
        Messager.exceptionHandler = exceptionHandler;
    }

    /**
     * 获取异常处理器
     *
     * @return
     */
    public static ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * 判断信使类是否已经完成初始化
     *
     * @param namespace 命名空间
     * @return TRUE:已完成初始化;FALSE:未完成初始化;
     */
    public static boolean isInit(final String namespace) {
        return namespaceMessagerHandlerMap.containsKey(namespace);
    }

    /**
     * 初始化信使
     *
     * @param namespace      命名空间
     * @param messageHandler 信使处理器
     */
    public static void init(final String namespace,
                            final MessageHandler messageHandler) {
        namespaceMessagerHandlerMap.putIfAbsent(namespace, messageHandler);
    }

    /**
     * 注册全局动作
     *
     * @param moduleName 动作名称
     * @param action     动作
     */
    public static void registerAction(final String moduleName, Action action) {
        Action action1 = actionMap.putIfAbsent(moduleName, action);
        if (action1 != null) {
            System.err.println("duplicate register action:" + moduleName);
        }
    }

    /**
     * 执行动作
     *
     * @param actionName 动作名称
     * @param args       参数
     */
    public static Object doAction(final String moduleName, final String actionName, Object... args) {
        Action action = actionMap.get(moduleName);
        if (action == null) {
            return null;
        }
        return action.onAction(actionName, args);
    }

    /**
     * 清理信使钩子方法
     *
     * @param namespace 命名空间
     */
    public synchronized static void clean(final String namespace) {
        namespaceMessagerHandlerMap.remove(namespace);
    }


    // 全局序列
    private static final AtomicInteger sequenceRef = new AtomicInteger(1000);

    /**
     * 生成全局唯一序列，
     * 在Simulator中允许多个命名空间的存在，不同的命名空间下listenerId/objectId将会被植入到同一份字节码中，
     * 此时需要用全局的ID生成策略规避不同的命名空间
     *
     * @return 全局自增序列
     */
    public static int nextSequence() {
        return sequenceRef.getAndIncrement();
    }


    private static void handleException(Throwable cause) throws Throwable {
        if (isMessagerThrows) {
            throw cause;
        } else {
            if (exceptionHandler != null) {
                exceptionHandler.handleException(cause, "", null);
            } else {
                cause.printStackTrace();
            }
        }
    }

    public static void invokeOnCallBefore(final int lineNumber,
                                          final boolean isInterface,
                                          final String owner,
                                          final String name,
                                          final String desc,
                                          final Class clazz,
                                          final String namespace,
                                          final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null != messageHandler) {
                messageHandler.handleOnCallBefore(listenerId, clazz, isInterface, lineNumber, owner, name, desc);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static void invokeOnCallReturn(final boolean isInterface,
                                          final Class clazz,
                                          final String namespace,
                                          final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null != messageHandler) {
                messageHandler.handleOnCallReturn(listenerId, clazz, isInterface);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static void invokeOnCallThrows(final Throwable e,
                                          final boolean isInterface,
                                          final Class clazz,
                                          final String namespace,
                                          final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null != messageHandler) {
                messageHandler.handleOnCallThrows(listenerId, clazz, isInterface, e);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static void invokeOnLine(final int lineNumber,
                                    final Class clazz,
                                    final String namespace,
                                    final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null != messageHandler) {
                messageHandler.handleOnLine(listenerId, clazz, lineNumber);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static Result invokeOnBefore(final Object[] argumentArray,
                                        final String namespace,
                                        final int listenerId,
                                        final String listenerClass, //只是为了排查时更加方便,所以在字节码增强时将注入的 listener 类名也写到字节码中
                                        final Class clazz,
                                        final String javaMethodName,
                                        final String javaMethodDesc,
                                        final Object target) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null == messageHandler) {
                return Result.RESULT_NONE;
            }
            return messageHandler.handleOnBefore(
                    listenerId, argumentArray,
                    clazz,
                    javaMethodName,
                    javaMethodDesc,
                    target
            );
        } catch (Throwable cause) {
            handleException(cause);
            return Result.RESULT_NONE;
        }
    }

    public static Result invokeOnReturn(final Object object,
                                        final Class clazz,
                                        final String namespace,
                                        final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null == messageHandler) {
                return Result.RESULT_NONE;
            }
            return messageHandler.handleOnReturn(listenerId, clazz, object);
        } catch (Throwable cause) {
            handleException(cause);
            return Result.RESULT_NONE;
        }
    }

    public static Result invokeOnThrows(final Throwable throwable,
                                        final Class clazz,
                                        final String namespace,
                                        final int listenerId) throws Throwable {
        try {
            final MessageHandler messageHandler = namespaceMessagerHandlerMap.get(namespace);
            if (null == messageHandler) {
                return Result.RESULT_NONE;
            }
            return messageHandler.handleOnThrows(listenerId, clazz, throwable);
        } catch (Throwable cause) {
            handleException(cause);
            return Result.RESULT_NONE;
        }
    }
}
