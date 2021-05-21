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
package com.pamirs.pradar;


import com.pamirs.pradar.exception.PradarException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 提供给InvokeContext 共用的公共类
 */
abstract class AbstractContext extends BaseContext {

    public final static Logger LOGGER = LoggerFactory.getLogger(AbstractContext.class);

    String remoteIp = PradarCoreUtils.EMPTY_STRING;
    String port = "";

    long startTime = 0L;
    long requestSize = 0L;
    long responseSize = 0L;
    /**
     * 结果码
     */
    String resultCode = ResultCode.INVOKE_RESULT_SUCCESS;
    /**
     * 会通过 RPC 调用中传递的用户属性，会在兄弟间、父子间传递
     */
    Map<String, String> attributes = null;

    /**
     * 完全不会通过 RPC 调用传递的本地属性，只属于本调用
     */
    Map<String, String> localAttributes = null;

    /**
     * 是否有错误
     */
    volatile boolean hasError;

    // service receiver
    AbstractContext(String _traceId, String _traceAppName, String _invokeId) {
        super(_traceId, _traceAppName, _invokeId);
    }

    AbstractContext(String _traceId, String _traceAppName, String _invokeId,
                    String traceMethod, String traceServiceName) {
        super(_traceId, _traceAppName, _invokeId, traceMethod, traceServiceName);
    }

    /**
     * 允许外部修改部分辅助的统计字段，方便异步回调时设置
     */
    public long getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(long requestSize) {
        this.requestSize = requestSize;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    /*
     *
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void destroy() {
        if (attributes != null) {
            attributes = null;
        }
        if (localAttributes != null) {
            localAttributes = null;
        }
        request = null;
        response = null;
    }

    //1/1000 trace will be logged
    public static boolean isForceTraced(String id) {
        if (id == null) {
            return false;
        }
        if (PradarSwitcher.getMustSamplingInterval() == 1000 && ('0' == id.charAt(id.length() - 6))
                && ('0' == id.charAt(id.length() - 7)) && ('0' == id.charAt(id.length() - 8))) {
            return true;
        } else if (PradarSwitcher.getMustSamplingInterval() == 100 && ('0' == id.charAt(id.length() - 6)) && ('0' == id.charAt(id.length() - 7))) {
            return true;
        } else if (PradarSwitcher.getMustSamplingInterval() == 10 && ('0' == id.charAt(id.length() - 6))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查当前上下文是否被采样，有效范围在 [1, 9999] 之间，超出范围的数值都作为全采样处理。
     *
     * @return <code>true</code> 则需要输出日志，<code>false</code> 不输出
     */
    public boolean isTraceSampled() {
        if (traceId == null) {
            return false;
        }

        if (Pradar.isDebug()) {
            return true;
        }

        //一定有一部分的trace id 被全量采集
        if (isForceTraced(traceId)) {
            return true;
        }

        final int si = PradarSwitcher.getSamplingInterval();
        if (si <= 1 || si >= 10000) {
            return true;
        }
        if (traceId.length() < 25) {
            return traceId.hashCode() % si == 0;
        }
        int count = traceId.charAt(21) - '0';
        count = count * 10 + traceId.charAt(22) - '0';
        count = count * 10 + traceId.charAt(23) - '0';
        count = count * 10 + traceId.charAt(24) - '0';
        return count % si == 0;
    }

    void logContextData(StringBuilder appender) {
        final boolean appendAttributes = this.attributes != null && !this.attributes.isEmpty();
        final boolean appendLocalAttributes = this.localAttributes != null && !this.localAttributes.isEmpty();
        if (!appendAttributes && !appendLocalAttributes) {
            return;
        }
        appender.append("|@");
        int startLen = appender.length();
        if (appendAttributes) {
            doAppendUserData(appender, startLen);
        }
        appender.append("|@");
        if (appendLocalAttributes) {
            doAppendLocalAttributes(appender, startLen);
        }
    }

    protected void doAppendUserData(StringBuilder appender, int startLen) {
        for (Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (PradarCoreUtils.isNotBlank(key) && value != null) {
                appender.append(key).append("@").append(PradarCoreUtils.makeLogSafe(value)).append("@");
            }
        }
    }

    protected void doAppendLocalAttributes(StringBuilder appender, int startLen) {
        for (Entry<String, String> entry : localAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (PradarCoreUtils.isNotBlank(key) && value != null) {
                // 在 key 前面加 @ 来区分是否本地属性
                appender.append('@').append(PradarCoreUtils.makeLogSafe(key)).append(Pradar.KV_SEPARATOR2).append(PradarCoreUtils.makeLogSafe(value))
                        .append(Pradar.ENTRY_SEPARATOR);
            }
        }
    }

    /**
     * 一组 user data 的 key 和 value 加起来总长度不超过64,其中 key 长度不超过16，value 长度不超过48
     */
    public String putUserData(String key, String value) {
        // 透传数据的限制
        if (PradarCoreUtils.isBlank(key) || key.length() > Pradar.MAX_USER_DATA_KEY_SIZE) {
            LOGGER.error("[ERROR] userData is not accepted since key is blank or too long: key: {} value: {}", key, value);
            throw new PradarException("[ERROR] localData is not accepted since key is blank or too long: key: " + key + " value: " + value);
        }
        if (value != null && value.length() > Pradar.MAX_USER_DATA_VALUE_SIZE) {
            LOGGER.warn("[ERROR] userData is not accepted since value is too long: key:{} value: {}", key, value);
            throw new PradarException("[ERROR] localData is not accepted since value is blank or too long: key: " + key + " value: " + value);
        }
        if (attributes == null) {
            attributes = new LinkedHashMap<String, String>();
        }
        return attributes.put(key, value);
    }

    /**
     *
     */
    public String removeUserData(String key) {
        if (attributes != null) {
            return attributes.remove(key);
        }
        return null;
    }

    /**
     *
     */
    public String getUserData(String key) {
        return attributes != null ? attributes.get(key) : null;
    }

    /**
     * 判断是否存在 key
     *
     * @param key key
     * @return true|false
     */
    public boolean hasUserData(String key) {
        return attributes == null ? false : attributes.containsKey(key);
    }

    /**
     *
     */
    public Map<String, String> getUserDataMap() {
        // 涉及透传，不允许外部修改
        return attributes == null
                ? Collections.<String, String>emptyMap()
                : Collections.<String, String>unmodifiableMap(attributes);
    }

    /**
     * 一组 local data 的 key 和 value 加起来总长度不超过64,其中 key 长度不超过16，value 长度不超过48
     */
    public String putLocalAttribute(String key, String value) {
        // 透传数据的限制
        if (PradarCoreUtils.isBlank(key) || key.length() > Pradar.MAX_LOCAL_DATA_KEY_SIZE) {
            LOGGER.error("[ERROR] localData is not accepted since key is blank or too long: key: {} value: {}", key, value);
            throw new PradarException("[ERROR] localData is not accepted since key is blank or too long: key: " + key + " value: " + value);
        }
        if (value != null && value.length() > Pradar.MAX_LOCAL_DATA_VALUE_SIZE) {
            LOGGER.warn("[ERROR] localData is not accepted since value is too long: key:{} value: {}", key, value);
            throw new PradarException("[ERROR] localData is not accepted since value is blank or too long: key: " + key + " value: " + value);
        }
        if (localAttributes == null) {
            localAttributes = new LinkedHashMap<String, String>();
        }
        return localAttributes.put(key, value);
    }

    /**
     *
     */
    public String removeLocalAttribute(String key) {
        if (localAttributes != null) {
            return localAttributes.remove(key);
        }
        return null;
    }

    /**
     *
     */
    public String getLocalAttribute(String key) {
        return localAttributes != null ? localAttributes.get(key) : null;
    }

    /**
     *
     */
    public Map<String, String> getLocalAttributeMap() {
        if (localAttributes == null) {
            localAttributes = new LinkedHashMap<String, String>();
        }
        // 因为不涉及透传，允许外部修改 localAttributes
        return localAttributes;
    }

    /**
     * 导出需要透传的 UserData
     */
    public String exportUserData() {
        Map<String, String> userData = this.attributes;
        if (userData == null || userData.isEmpty()) {
            return null;
        }
        StringBuilder appender = new StringBuilder(256);
        doAppendUserData(appender, 0);
        if (appender.length() == 0) {
            return null;
        }
        return appender.toString();
    }

    /**
     * 导入透传过来的 UserData
     */
    public void importUserData(String userData) {
        if (PradarCoreUtils.isNotBlank(userData) && PradarSwitcher.isUserDataEnabled()) {
            String[] entries = PradarCoreUtils.split(userData, '@');
            Map<String, String> map = new LinkedHashMap<String, String>(entries.length);
            for (int i = 0, len = entries.length / 2; i < len; i += 2) {
                String key = StringUtils.trim(entries[i]);
                String value = StringUtils.trim(entries[i + 1]);
                if (StringUtils.isNotBlank(key)) {
                    map.put(key, value);
                }
            }
            if (!map.isEmpty()) {
                this.attributes = map;
            }
        }
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}
