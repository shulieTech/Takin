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
package com.pamirs.attach.plugin.mock.interceptor;

import com.pamirs.attach.plugin.mock.utils.GroovyUtils;
import com.pamirs.pradar.Pradar;
import com.shulie.instrument.simulator.api.ProcessController;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MockAdviceListener extends AdviceListener {
    private final static Logger logger = LoggerFactory.getLogger(MockAdviceListener.class);
    private final static Logger mockLogger = LoggerFactory.getLogger("MOCK-LOGGER");

    private String scriptContent;
    private int scriptType;
    private AdviceListener adviceListener;

    public MockAdviceListener(int scriptType, String scriptContent) {
        this.scriptType = scriptType;
        this.scriptContent = scriptContent;
        if (scriptType == 2) {
            Class clazz = GroovyUtils.compile(scriptContent);
            adviceListener = Reflect.on(clazz).create().get();
        }
    }

    @Override
    public void before(Advice advice) throws Throwable {
        if (Pradar.isClusterTest()) {
            if (scriptType == 1) {
                Map<String, Object> binding = new HashMap<String, Object>(4, 1.0f);
                binding.put("args", advice.getParameterArray());
                binding.put("target", advice.getTarget());
                binding.put("classLoader", advice.getClassLoader());
                binding.put("logger", mockLogger);

                Object result = GroovyUtils.execute(scriptContent, binding);
                ProcessController.returnImmediately(result);
            } else if (scriptType == 2) {
                if (adviceListener == null) {
                    logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
                } else {
                    adviceListener.before(advice);
                }
            }
        }

    }

    @Override
    public void after(Advice advice) throws Throwable {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.after(advice);
            }
        }
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.afterThrowing(advice);
            }
        }
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.afterReturning(advice);
            }
        }
    }

    @Override
    public void beforeCall(Advice advice, int callLineNum, boolean isInterface, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.beforeCall(advice, callLineNum, isInterface, callJavaClassName, callJavaMethodName, callJavaMethodDesc);
            }
        }
    }

    @Override
    public void afterCallReturning(Advice advice, int callLineNum, boolean isInterface, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.afterCallReturning(advice, callLineNum, isInterface, callJavaClassName, callJavaMethodName, callJavaMethodDesc);
            }
        }
    }

    @Override
    public void afterCallThrowing(Advice advice, int callLineNum, boolean isInterface, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, Throwable callThrowable) {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.afterCallThrowing(advice, callLineNum, isInterface, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowable);
            }
        }
    }

    @Override
    public void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, Throwable callThrowable) {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.afterCall(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowable);
            }
        }
    }

    @Override
    public void beforeLine(Advice advice, int lineNum) {
        if (scriptType == 2) {
            if (adviceListener == null) {
                logger.error("mock: script execute err.can't initialize groovy class:{}", scriptContent);
            } else {
                adviceListener.beforeLine(advice, lineNum);
            }
        }
    }

}
