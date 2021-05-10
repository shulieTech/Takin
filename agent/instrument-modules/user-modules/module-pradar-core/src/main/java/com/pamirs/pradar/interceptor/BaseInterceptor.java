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
package com.pamirs.pradar.interceptor;

import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.debug.DebugTestInfoPusher;
import com.pamirs.pradar.debug.model.DebugTestInfo;
import com.pamirs.pradar.json.ResultSerializer;
import com.shulie.instrument.simulator.api.ProcessControlException;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截器
 * <pre>
 *     注意:实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 *
 * @author fabing.zhaofb
 */
abstract class BaseInterceptor extends AdviceListener {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);

    protected InterceptorScope interceptorScope;

    public void setInterceptorScope(InterceptorScope interceptorScope) {
        this.interceptorScope = interceptorScope;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        DebugTestInfo debugTestInfo = buildDebugTestInfo(advice, "beforeFirst", null);
        Throwable e = null;
        try {
            doBefore(advice);
        } catch (ProcessControlException t) {
            throw t;
        } catch (Throwable t) {
            e = t;
            throw t;
        } finally {
            processDebugRecord(debugTestInfo);
            recordDebugInfo(advice, "beforeLast", e);
        }
    }

    private static String serializeObject(Object target) {
        if (target == null) {
            return StringUtils.EMPTY;
        }
        try {
            return ResultSerializer.serializeObject(target, 2);
        } catch (Throwable e) {
            return StringUtils.EMPTY;
        }
    }

    private void processDebugRecord(DebugTestInfo debugTestInfo) {
        if (debugTestInfo == null) {
            return;
        }
        if (!Pradar.isDebug()) {
            return;
        }
        debugTestInfo.setTraceId(Pradar.getTraceId());
        debugTestInfo.setRpcId(Pradar.getInvokeId());
        debugTestInfo.setLogType(Pradar.getLogType());
        DebugTestInfoPusher.addDebugInfo(debugTestInfo);
    }

    private DebugTestInfo buildDebugTestInfo() {
        if (!Pradar.isDebug()) {
            return null;
        }
        DebugTestInfo debugTestInfo = new DebugTestInfo();
        debugTestInfo.setAppName(AppNameUtils.appName());
        debugTestInfo.setAgentId(Pradar.getAgentId());
        debugTestInfo.setTraceId(Pradar.getTraceId());
        debugTestInfo.setRpcId(Pradar.getInvokeId());
        debugTestInfo.setLogType(Pradar.getLogType());
        return debugTestInfo;
    }

    private void recordDebugInfo(final Advice advice, final String method, final Throwable t) {
        DebugTestInfo debugTestInfo = buildDebugTestInfo(advice, method, t);
        if (debugTestInfo != null) {
            DebugTestInfoPusher.addDebugInfo(debugTestInfo);
        }
    }

    private void recordDebugInfo(final DebugTestInfo debugTestInfo, final Advice advice, final String method, final Throwable t) {
        if (!Pradar.isDebug()) {
            return;
        }
        if (debugTestInfo == null) {
            return;
        }
        final String interceptorClassName = getClass().getName();
        final String targetClass = advice.getTargetClass().getName();
        final String classLoader = advice.getClassLoader().toString();
        final Object target = advice.getTarget();
        final Object[] args = advice.getParameterArray();
        final Object retObj = advice.getReturnObj();
        final Throwable throwable = advice.getThrowable();
        debugTestInfo.setLogCallback(new DebugTestInfo.LogCallback() {
            @Override
            public DebugTestInfo.Log getLog() {
                String targetString = serializeObject(target);
                String parameterArray = serializeObject(args);
                String returnObj = serializeObject(retObj);

                DebugTestInfo.Log log = new DebugTestInfo.Log();
                if (t != null || throwable != null) {
                    log.setLevel("ERROR");
                    if (t != null && throwable != null) {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, throwable: %s, interceptorError:%s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(throwable),
                                serializeObject(t)
                        ));
                    } else if (t != null) {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, interceptorError:%s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(t)
                        ));
                    } else {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, throwable: %s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(throwable)
                        ));
                    }
                } else {
                    log.setLevel("INFO");
                    log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s",
                            interceptorClassName,
                            method,
                            targetClass,
                            classLoader,
                            targetString,
                            parameterArray,
                            returnObj));
                }
                return log;
            }
        });
        DebugTestInfoPusher.addDebugInfo(debugTestInfo);
    }

    private DebugTestInfo buildDebugTestInfo(final Advice advice, final String method, final Throwable t) {
        if (!Pradar.isDebug()) {
            return null;
        }

        DebugTestInfo debugTestInfo = new DebugTestInfo();
        debugTestInfo.setTraceId(Pradar.getTraceId());
        debugTestInfo.setRpcId(Pradar.getInvokeId());
        debugTestInfo.setLogType(Pradar.getLogType());
        debugTestInfo.setAgentId(Pradar.getAgentId());
        debugTestInfo.setAppName(AppNameUtils.appName());
        final String interceptorClassName = getClass().getName();
        final String targetClass = advice.getTargetClass().getName();
        final String classLoader = advice.getClassLoader().toString();
        final Object target = advice.getTarget();
        final Object[] args = advice.getParameterArray();
        final Object retObj = advice.getReturnObj();
        final Throwable throwable = advice.getThrowable();

        debugTestInfo.setLogCallback(new DebugTestInfo.LogCallback() {
            @Override
            public DebugTestInfo.Log getLog() {
                String targetString = serializeObject(target);
                String parameterArray = serializeObject(args);
                String returnObj = serializeObject(retObj);

                DebugTestInfo.Log log = new DebugTestInfo.Log();
                if (t != null || throwable != null) {
                    log.setLevel("ERROR");
                    if (t != null && throwable != null) {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, throwable: %s, interceptorError:%s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(throwable),
                                serializeObject(t)
                        ));
                    } else if (t != null) {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, interceptorError:%s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(t)
                        ));
                    } else {
                        log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s, throwable: %s",
                                interceptorClassName,
                                method,
                                targetClass,
                                classLoader,
                                targetString,
                                parameterArray,
                                returnObj,
                                serializeObject(throwable)
                        ));
                    }
                } else {
                    log.setLevel("INFO");
                    log.setContent(String.format("%s[%s]: targetClass: %s, classLoader: %s, target: %s, parameterArray: %s, returnObj: %s",
                            interceptorClassName,
                            method,
                            targetClass,
                            classLoader,
                            targetString,
                            parameterArray,
                            returnObj));
                }
                return log;
            }
        });
        return debugTestInfo;
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        recordDebugInfo(advice, "afterFirst", null);
        DebugTestInfo debugTestInfo = buildDebugTestInfo();
        Throwable throwable = null;
        try {
            doAfter(advice);
            if (advice.attachment() != null && advice.attachment() instanceof Throwable) {
                throwable = advice.attachment();
                advice.attach(null);
            }
        } catch (ProcessControlException e) {
            throw e;
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            recordDebugInfo(debugTestInfo, advice, "afterLast", throwable);
        }
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        buildDebugTestInfo(advice, "exceptionFirst", null);
        DebugTestInfo debugTestInfo = buildDebugTestInfo();
        Throwable throwable = null;
        try {
            doException(advice);
        } catch (ProcessControlException e) {
            throw e;
        } catch (Throwable t) {
            throwable = t;
            throw t;
        } finally {
            recordDebugInfo(debugTestInfo, advice, "exceptionLast", throwable);
        }
    }

    /**
     * 方法调用开始
     *
     * @param advice
     * @throws Throwable
     */
    public void doBefore(Advice advice) throws Throwable {

    }

    /**
     * 方法调用结束
     * 如果在 {@link #doBefore(Advice)} 时
     * 强行返回结果或者强行抛出异常，则此方法不会再被执行
     *
     * @param advice
     * @throws Throwable
     */
    public void doAfter(Advice advice) throws Throwable {

    }

    /**
     * 方法出现异常时调用
     * 如果在 {@link #doBefore(Advice)} 时
     * 强行返回结果或者强行抛出异常，则此方法不会再被执行
     *
     * @param advice
     * @throws Throwable
     */
    public void doException(Advice advice) throws Throwable {

    }
}

