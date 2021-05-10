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
package com.shulie.instrument.simulator.module.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

abstract public class ThreadUtil {

    public static TraceInfo getTraceInfo(Thread currentThread) {
        try {
            // access to Thread#threadlocals field
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocalMap = threadLocalsField.get(currentThread);
            // access to ThreadLocal$ThreadLocalMap#table filed
            Field tableFiled = threadLocalMap.getClass().getDeclaredField("table");
            tableFiled.setAccessible(true);
            Object[] tableEntries = (Object[]) tableFiled.get(threadLocalMap);
            for (Object entry : tableEntries) {
                if (entry == null) {
                    continue;
                }
                // access to ThreadLocal$ThreadLocalMap$Entry#value field
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object threadLocalValue = valueField.get(entry);
                TraceInfo traceInfo = new TraceInfo();
                if (threadLocalValue != null && "com.pamirs.pradar.RpcContext_inner".equals(threadLocalValue.getClass().getName())) {
                    // finally we got the chance to access trace id
                    Method getTraceIdMethod = threadLocalValue.getClass().getMethod("getTraceId");
                    getTraceIdMethod.setAccessible(true);
                    String traceId = (String) getTraceIdMethod.invoke(threadLocalValue);
                    traceInfo.setTraceId(traceId);
                    // get rpc id
                    Method getRpcIdMethod = threadLocalValue.getClass().getMethod("getRpcId");
                    getTraceIdMethod.setAccessible(true);
                    String rpcId = (String) getRpcIdMethod.invoke(threadLocalValue);
                    traceInfo.setRpcId(rpcId);

                    Method isClusterTestMethod = threadLocalValue.getClass().getMethod("isClusterTest");
                    isClusterTestMethod.setAccessible(true);
                    boolean isClusterTest = (Boolean) isClusterTestMethod.invoke(threadLocalValue);
                    traceInfo.setClusterTest(isClusterTest);

                    return traceInfo;
                }
            }
        } catch (Throwable e) {
            // ignore
        }
        return null;
    }

}
