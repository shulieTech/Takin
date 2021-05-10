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
package com.shulie.instrument.module.pradar.core.service;

import com.pamirs.pradar.InvokeContext;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.internal.IPradarInternalService;

import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/24 9:38 下午
 */
public class DefaultPradarInternalService implements IPradarInternalService {
    @Override
    public String addClusterTestPrefixUpper(String value) {
        return Pradar.addClusterTestPrefixUpper(value);
    }

    @Override
    public String addClusterTestPrefixLower(String value) {
        return Pradar.addClusterTestPrefixLower(value);
    }

    @Override
    public String addClusterTestPrefixRodLower(String value) {
        return Pradar.addClusterTestPrefixRodLower(value);
    }

    @Override
    public String addClusterTestPrefix(String value) {
        return Pradar.addClusterTestPrefix(value);
    }

    @Override
    public String addClusterTestSuffixUpper(String value) {
        return Pradar.addClusterTestSuffixUpper(value);
    }

    @Override
    public String addClusterTestSuffixLower(String value) {
        return Pradar.addClusterTestSuffixLower(value);
    }

    @Override
    public String addClusterTestSuffix(String value) {
        return Pradar.addClusterTestSuffix(value);
    }

    @Override
    public boolean isContainsClusterTest(String value) {
        return Pradar.isContainsClusterTest(value);
    }

    @Override
    public boolean isContainsClusterTest(String value, String prefix) {
        return Pradar.isContainsClusterTest(value, prefix);
    }

    @Override
    public String removeClusterTestPrefix(String value) {
        return Pradar.removeClusterTestPrefix(value);
    }

    @Override
    public boolean isClusterTestPrefix(String value, String prefix) {
        return Pradar.isClusterTestPrefix(value, prefix);
    }

    @Override
    public boolean isClusterTestPrefix(String value) {
        return Pradar.isClusterTestPrefix(value);
    }

    @Override
    public boolean isClusterTestPrefixRod(String value) {
        return Pradar.isClusterTestPrefixRod(value);
    }

    @Override
    public boolean isClusterTestSuffix(String value) {
        return Pradar.isClusterTestSuffix(value);
    }

    @Override
    public void startTrace(String traceId, String serviceName, String methodName) {
        Pradar.startTrace(traceId, serviceName, methodName);
    }

    @Override
    public void startTrace(String traceId, String invokeId, String serviceName, String methodName) {
        Pradar.startTrace(traceId, invokeId, serviceName, methodName);
    }

    @Override
    public void setClusterTest(boolean isClusterTest) {
        Pradar.setClusterTest(isClusterTest);
    }

    @Override
    public boolean isClusterTest() {
        return Pradar.isClusterTest();
    }

    @Override
    public void setDebug(boolean b) {
        Pradar.setDebug(b);
    }

    @Override
    public boolean isDebug() {
        return Pradar.isDebug();
    }

    @Override
    public void endTrace() {
        Pradar.endTrace();
    }

    @Override
    public void endTrace(String resultCode, int type) {
        Pradar.endTrace(resultCode, type);
    }

    @Override
    public boolean hasInvokeContext() {
        return Pradar.hasInvokeContext();
    }

    @Override
    public boolean hasInvokeContext(Map<String, String> ctx) {
        return Pradar.hasInvokeContext(ctx);
    }

    @Override
    public Object parseInvokeContext(Map<String, String> ctx) {
        return Pradar.fromMap(ctx);
    }

    @Override
    public String getMethod() {
        return Pradar.getMethod();
    }

    @Override
    public String getService() {
        return Pradar.getService();
    }

    @Override
    public void setInvokeContextWithParent(Object rpcCtx) {
        Pradar.setInvokeContextWithParent(rpcCtx);
    }

    @Override
    public void setInvokeContext(Object invokeCtx) {
        Pradar.setInvokeContext(invokeCtx);
    }

    @Override
    public void clearInvokeContext() {
        Pradar.clearInvokeContext();
    }

    @Override
    public Object popInvokeContext() {
        return Pradar.popInvokeContext();
    }

    @Override
    public void commitInvokeContext(Object ctx) {
        if (ctx instanceof InvokeContext) {
            Pradar.commitInvokeContext((InvokeContext) ctx);
        }
    }

    @Override
    public void upAppName(String upAppName) {
        Pradar.upAppName(upAppName);
    }

    @Override
    public void setLogType(int logType) {
        Pradar.setLogType(logType);
    }

    @Override
    public void middlewareName(String middlewareName) {
        Pradar.middlewareName(middlewareName);
    }

    @Override
    public String getMiddlewareName() {
        return Pradar.getMiddlewareName();
    }

    @Override
    public void startClientInvoke(String serviceName, String methodName) {
        Pradar.startClientInvoke(serviceName, methodName);
    }

    @Override
    public void endClientInvoke(String resultCode, int type) {
        Pradar.endClientInvoke(resultCode, type);
    }

    @Override
    public void startServerInvoke(String service, String method, String remoteAppName, Object ctxObj) {
        Pradar.startServerInvoke(service, method, remoteAppName, ctxObj);
    }

    @Override
    public void startServerInvoke(String service, String method, Object ctxObj) {
        Pradar.startServerInvoke(service, method, ctxObj);
    }

    @Override
    public void startServerInvoke(String service, String method, String remoteAppName) {
        Pradar.startServerInvoke(service, method, remoteAppName);
    }

    @Override
    public void endServerInvoke(int type) {
        Pradar.endServerInvoke(type);
    }

    @Override
    public void endServerInvoke(String resultCode, int type) {
        Pradar.endServerInvoke(resultCode, type);
    }

    @Override
    public String getTraceAppName() {
        return Pradar.getTraceAppName();
    }

    @Override
    public String getUpAppName() {
        return Pradar.getUpAppName();
    }

    @Override
    public String getRemoteAppName() {
        return Pradar.getRemoteAppName();
    }

    @Override
    public String getRemoteIp() {
        return Pradar.getRemoteIp();
    }

    @Override
    public String getTraceId() {
        return Pradar.getTraceId();
    }

    @Override
    public String getInvokeId() {
        return Pradar.getInvokeId();
    }

    @Override
    public Integer getLogType() {
        return Pradar.getLogType();
    }

    @Override
    public void request(Object request) {
        Pradar.request(request);
    }

    @Override
    public void response(Object response) {
        Pradar.response(response);
    }

    @Override
    public void attribute(String key, String value) {
        Pradar.attribute(key, value);
    }

    @Override
    public String getUserData(String key) {
        return Pradar.getUserData(key);
    }

    @Override
    public boolean putUserData(String key, String value) {
        return Pradar.putUserData(key, value);
    }

    @Override
    public boolean hasUserData(String key) {
        return Pradar.hasUserData(key);
    }

    @Override
    public String removeUserData(String key) {
        return Pradar.removeUserData(key);
    }

    @Override
    public Map<String, String> getUserDataMap() {
        return Pradar.getUserDataMap();
    }

    @Override
    public String exportUserData() {
        return Pradar.exportUserData();
    }

    @Override
    public void callBack(String msg) {
        Pradar.callBack(msg);
    }

    @Override
    public void requestSize(long size) {
        Pradar.requestSize(size);
    }

    @Override
    public void remoteIp(String remoteIp) {
        Pradar.remoteIp(remoteIp);
    }

    @Override
    public void remotePort(String port) {
        Pradar.remotePort(port);
    }

    @Override
    public void remotePort(int port) {
        Pradar.remotePort(port);
    }

    @Override
    public void responseSize(long size) {
        Pradar.responseSize(size);
    }

    @Override
    public String getProperty(String key) {
        return Pradar.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return Pradar.getProperty(key, defaultValue);
    }

    @Override
    public boolean isRequestOn() {
        return Pradar.isRequestOn();
    }

    @Override
    public boolean isResponseOn() {
        return Pradar.isResponseOn();
    }

    @Override
    public boolean isExceptionOn() {
        return Pradar.isExceptionOn();
    }

    @Override
    public Integer getPluginRequestSize() {
        return Pradar.getPluginRequestSize();
    }

    @Override
    public Integer getPluginResponseSize() {
        return Pradar.getPluginResponseSize();
    }

    @Override
    public Integer getIntProperty(String key, Integer defaultValue) {
        return Pradar.getIntProperty(key, defaultValue);
    }

    @Override
    public Integer getIntProperty(String key) {
        return Pradar.getIntProperty(key);
    }

    @Override
    public Long getLongProperty(String key, Long defaultValue) {
        return Pradar.getLongProperty(key, defaultValue);
    }

    @Override
    public Long getLongProperty(String key) {
        return Pradar.getLongProperty(key);
    }

    @Override
    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        return Pradar.getBooleanProperty(key, defaultValue);
    }

    @Override
    public Boolean getBooleanProperty(String key) {
        return Pradar.getBooleanProperty(key);
    }
}
