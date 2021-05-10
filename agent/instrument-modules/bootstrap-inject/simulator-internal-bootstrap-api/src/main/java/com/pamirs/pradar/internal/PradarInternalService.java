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
package com.pamirs.pradar.internal;

import java.util.Map;

/**
 * Pradar 内部服务，不对外提供服务，只对内部插件开放，用于特定的业务如需要将类注入到业务中，则这些类需要在 biz-classloader-inject
 * 模块中定义，并且交由业务 classloader 进行加载，此时在这些类中如果想直接访问 com.pamirs.pradar.Pradar 是访问不到的，此时需要
 * 借助于此服务来代理访问 com.pamirs.pradar.Pradar 来进行对应的一些操作
 * <p>
 * 此类只用于内部模块使用，只用于内部模块使用，只用于内部模块使用，重要的事情说三遍
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/24 9:06 下午
 */
public class PradarInternalService {

    private static IPradarInternalService service;
    private static IConfigFetcher configFetcher;

    public static void registerService(IPradarInternalService pradarInternalService) {
        service = pradarInternalService;
    }

    public static void registerConfigFetcher(IConfigFetcher fetcher) {
        configFetcher = fetcher;
    }

    public static String addClusterTestPrefixUpper(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestPrefixUpper(value);
    }


    public static String addClusterTestPrefixLower(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestPrefixLower(value);
    }


    public static String addClusterTestPrefixRodLower(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestPrefixRodLower(value);
    }


    public static String addClusterTestPrefix(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestPrefix(value);
    }


    public static String addClusterTestSuffixUpper(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestSuffixUpper(value);
    }


    public static String addClusterTestSuffixLower(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestSuffixLower(value);
    }


    public static String addClusterTestSuffix(String value) {
        if (service == null) {
            return value;
        }
        return service.addClusterTestSuffix(value);
    }


    public static boolean isContainsClusterTest(String value) {
        if (service == null) {
            return false;
        }
        return service.isContainsClusterTest(value);
    }


    public static boolean isContainsClusterTest(String value, String prefix) {
        if (service == null) {
            return false;
        }
        return service.isContainsClusterTest(value, prefix);
    }


    public static String removeClusterTestPrefix(String value) {
        if (service == null) {
            return value;
        }
        return service.removeClusterTestPrefix(value);
    }


    public static boolean isClusterTestPrefix(String value, String prefix) {
        if (service == null) {
            return false;
        }
        return service.isClusterTestPrefix(value, prefix);
    }


    public static boolean isClusterTestPrefix(String value) {
        if (service == null) {
            return false;
        }
        return service.isClusterTestPrefix(value);
    }


    public static boolean isClusterTestPrefixRod(String value) {
        if (service == null) {
            return false;
        }
        return service.isClusterTestPrefixRod(value);
    }


    public static boolean isClusterTestSuffix(String value) {
        if (service == null) {
            return false;
        }
        return service.isClusterTestSuffix(value);
    }


    public static void startTrace(String traceId, String serviceName, String methodName) {
        if (service == null) {
            return;
        }
        service.startTrace(traceId, serviceName, methodName);
    }


    public static void startTrace(String traceId, String invokeId, String serviceName, String methodName) {
        if (service == null) {
            return;
        }
        service.startTrace(traceId, invokeId, serviceName, methodName);
    }


    public static void setClusterTest(boolean isClusterTest) {
        if (service == null) {
            return;
        }
        service.setClusterTest(isClusterTest);
    }


    public static boolean isClusterTest() {
        if (service == null) {
            return false;
        }
        return service.isClusterTest();
    }


    public static void setDebug(boolean b) {
        if (service == null) {
            return;
        }
        service.setDebug(b);
    }


    public static boolean isDebug() {
        if (service == null) {
            return false;
        }
        return service.isDebug();
    }


    public static void endTrace() {
        if (service == null) {
            return;
        }
        service.endTrace();
    }


    public static void endTrace(String resultCode, int type) {
        if (service == null) {
            return;
        }
        service.endTrace(resultCode, type);
    }

    public static boolean hasInvokeContext() {
        if (service == null) {
            return false;
        }
        return service.hasInvokeContext();
    }

    public static boolean hasInvokeContext(Map<String, String> ctx) {
        if (service == null) {
            return false;
        }
        return service.hasInvokeContext(ctx);
    }

    public static Object parseInvokeContext(Map<String, String> ctx) {
        if (service == null) {
            return false;
        }
        return service.parseInvokeContext(ctx);
    }

    public static String getMethod() {
        if (service == null) {
            return null;
        }
        return service.getMethod();
    }


    public static String getService() {
        if (service == null) {
            return null;
        }
        return service.getService();
    }


    public static void setInvokeContextWithParent(Object rpcCtx) {
        if (service == null) {
            return;
        }
        service.setInvokeContextWithParent(rpcCtx);
    }


    public static void setInvokeContext(Object invokeCtx) {
        if (service == null) {
            return;
        }
        service.setInvokeContext(invokeCtx);
    }


    public static void clearInvokeContext() {
        if (service == null) {
            return;
        }
        service.clearInvokeContext();
    }


    public static Object popInvokeContext() {
        if (service == null) {
            return null;
        }
        return service.popInvokeContext();
    }


    public static void commitInvokeContext(Object ctx) {
        if (service == null) {
            return;
        }
        service.commitInvokeContext(ctx);
    }


    public static void upAppName(String upAppName) {
        if (service == null) {
            return;
        }
        service.upAppName(upAppName);
    }


    public static void setLogType(int logType) {
        if (service == null) {
            return;
        }
        service.setLogType(logType);
    }


    public static void middlewareName(String middlewareName) {
        if (service == null) {
            return;
        }
        service.middlewareName(middlewareName);
    }


    public static String getMiddlewareName() {
        if (service == null) {
            return null;
        }
        return service.getMiddlewareName();
    }


    public static void startClientInvoke(String serviceName, String methodName) {
        if (service == null) {
            return;
        }
        service.startClientInvoke(serviceName, methodName);
    }

    public static void endClientInvoke(String resultCode, int type) {
        if (service == null) {
            return;
        }
        service.endClientInvoke(resultCode, type);
    }


    public static void startServerInvoke(String serviceName, String method, String remoteAppName, Object ctxObj) {
        if (service == null) {
            return;
        }
        service.startServerInvoke(serviceName, method, remoteAppName, ctxObj);
    }


    public static void startServerInvoke(String serviceName, String method, Object ctxObj) {
        if (service == null) {
            return;
        }
        service.startServerInvoke(serviceName, method, ctxObj);
    }


    public static void startServerInvoke(String serviceName, String method, String remoteAppName) {
        if (service == null) {
            return;
        }
        service.startServerInvoke(serviceName, method, remoteAppName);
    }


    public static void endServerInvoke(int type) {
        if (service == null) {
            return;
        }
        service.endServerInvoke(type);
    }


    public static void endServerInvoke(String resultCode, int type) {
        if (service == null) {
            return;
        }
        service.endServerInvoke(resultCode, type);
    }


    public static String getTraceAppName() {
        if (service == null) {
            return null;
        }
        return service.getTraceAppName();
    }


    public static String getUpAppName() {
        if (service == null) {
            return null;
        }
        return service.getUpAppName();
    }


    public static String getRemoteAppName() {
        if (service == null) {
            return null;
        }
        return service.getRemoteAppName();
    }


    public static String getRemoteIp() {
        if (service == null) {
            return null;
        }
        return service.getRemoteIp();
    }


    public static String getTraceId() {
        if (service == null) {
            return null;
        }
        return service.getTraceId();
    }


    public static String getInvokeId() {
        if (service == null) {
            return null;
        }
        return service.getInvokeId();
    }


    public static Integer getLogType() {
        if (service == null) {
            return null;
        }
        return service.getLogType();
    }


    public static void request(Object request) {
        if (service == null) {
            return;
        }
        service.request(request);
    }


    public static void response(Object response) {
        if (service == null) {
            return;
        }
        service.response(response);
    }


    public static void attribute(String key, String value) {
        if (service == null) {
            return;
        }
        service.attribute(key, value);
    }


    public static String getUserData(String key) {
        if (service == null) {
            return null;
        }
        return service.getUserData(key);
    }


    public static boolean putUserData(String key, String value) {
        if (service == null) {
            return false;
        }
        return service.putUserData(key, value);
    }


    public static boolean hasUserData(String key) {
        if (service == null) {
            return false;
        }
        return service.hasUserData(key);
    }


    public static String removeUserData(String key) {
        if (service == null) {
            return null;
        }
        return service.removeUserData(key);
    }


    public static Map<String, String> getUserDataMap() {
        if (service == null) {
            return null;
        }
        return service.getUserDataMap();
    }


    public static String exportUserData() {
        if (service == null) {
            return null;
        }
        return service.exportUserData();
    }


    public static void callBack(String msg) {
        if (service == null) {
            return;
        }
        service.callBack(msg);
    }


    public static void requestSize(long size) {
        if (service == null) {
            return;
        }
        service.requestSize(size);
    }


    public static void remoteIp(String remoteIp) {
        if (service == null) {
            return;
        }
        service.remoteIp(remoteIp);
    }


    public static void remotePort(String port) {
        if (service == null) {
            return;
        }
        service.remotePort(port);
    }


    public static void remotePort(int port) {
        if (service == null) {
            return;
        }
        service.remotePort(port);
    }


    public static void responseSize(long size) {
        if (service == null) {
            return;
        }
        service.responseSize(size);
    }


    public static String getProperty(String key) {
        if (service == null) {
            return null;
        }
        return service.getProperty(key);
    }


    public static String getProperty(String key, String defaultValue) {
        if (service == null) {
            return null;
        }
        return service.getProperty(key, defaultValue);
    }


    public static boolean isRequestOn() {
        if (service == null) {
            return false;
        }
        return service.isRequestOn();
    }


    public static boolean isResponseOn() {
        if (service == null) {
            return false;
        }
        return service.isResponseOn();
    }


    public static boolean isExceptionOn() {
        if (service == null) {
            return false;
        }
        return service.isExceptionOn();
    }


    public static Integer getPluginRequestSize() {
        if (service == null) {
            return null;
        }
        return service.getPluginRequestSize();
    }


    public static Integer getPluginResponseSize() {
        if (service == null) {
            return null;
        }
        return service.getPluginResponseSize();
    }


    public static Integer getIntProperty(String key, Integer defaultValue) {
        if (service == null) {
            return null;
        }
        return service.getIntProperty(key, defaultValue);
    }


    public static Integer getIntProperty(String key) {
        if (service == null) {
            return null;
        }
        return service.getIntProperty(key);
    }


    public static Long getLongProperty(String key, Long defaultValue) {
        if (service == null) {
            return null;
        }
        return service.getLongProperty(key, defaultValue);
    }


    public static Long getLongProperty(String key) {
        if (service == null) {
            return null;
        }
        return service.getLongProperty(key);
    }


    public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        if (service == null) {
            return null;
        }
        return service.getBooleanProperty(key, defaultValue);
    }


    public static Boolean getBooleanProperty(String key) {
        if (service == null) {
            return null;
        }
        return service.getBooleanProperty(key);
    }

    public static void triggerConfigFetch() {
        if (configFetcher == null) {
            return;
        }
        configFetcher.triggerConfigFetch();
    }

    /**
     * 判断是否已经初始化完成
     *
     * @return
     */
    public static boolean isInited() {
        return service != null;
    }
}
