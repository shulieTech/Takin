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

import java.util.Collections;
import java.util.Map;

/**
 * Pradar 向外暴露的服务，在使用服务时需要确认当前压测服务是否可用
 * {@link #isClusterTestEnabled()}
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/11 2:08 下午
 */
public final class PradarService {
    /**
     * pradar 的 header 头前缀
     */
    static final public String HEADER_PREFIX = "pradar.header.prefix";
    /**
     * 获取 pradar header 的头前缀
     */
    static public final String PRADAR_HEADER_PREFIX = getPradarHeaderPrefix();

    /**
     * 上下文 invokeId 使用的 key
     */
    static public final String PRADAR_INVOKE_ID_KEY = PRADAR_HEADER_PREFIX + "p-pradar-rpcid";

    /**
     * 上下文 userData 使用的 key
     */
    static public final String PRADAR_USER_DATA_KEY = PRADAR_HEADER_PREFIX + "p-pradar-userdata";

    /**
     * 上下文 trace app name key
     */
    static public final String PRADAR_TRACE_APPNAME_KEY = PRADAR_HEADER_PREFIX + "p-pradar-appname";

    /**
     * 上下文 remote app name
     */
    static public final String PRADAR_REMOTE_APPNAME_KEY = PRADAR_HEADER_PREFIX + "p-pradar-reappname";

    /**
     * 上下文 上游 app name
     */
    static public final String PRADAR_UPSTREAM_APPNAME_KEY = PRADAR_HEADER_PREFIX + "p-pradar-upappname";

    /**
     * 上下文 logtype
     */
    static public final String PRADAR_LOG_TYPE_KEY = PRADAR_HEADER_PREFIX + "p-pradar-logtype";


    /**
     * 上下文 startTime
     */
    static public final String PRADAR_START_TIME_KEY = PRADAR_HEADER_PREFIX + "p-pradar-startTime";

    /**
     * 上下文 remote_ip
     */
    static public final String PRADAR_REMOTE_IP = PRADAR_HEADER_PREFIX + "p-pradar-remote-ip";


    /**
     * 上下文 traceid
     */
    static public final String PRADAR_TRACE_ID_KEY = PRADAR_HEADER_PREFIX + "p-pradar-traceid";

    /**
     * 上下文  压测标 key
     */
    static public final String PRADAR_CLUSTER_TEST_KEY = PRADAR_HEADER_PREFIX + "p-pradar-cluster-test";

    /**
     * 上下文 http 压测标 key
     */
    static public final String PRADAR_HTTP_CLUSTER_TEST_KEY = "User-Agent";

    /**
     * 上下文 debug 标 key
     */
    static public final String PRADAR_DEBUG_KEY = PRADAR_HEADER_PREFIX + "p-pradar-debug";

    /**
     * 服务名称,只用于内部传输，不进行远程传输
     */
    static public final String PRADAR_SERVICE_NAME = "p-pradar-service";

    /**
     * 方法名称,只用于内部传输，不进行远程传输
     */
    static public final String PRADAR_METHOD_NAME = "p-pradar-method";

    /**
     * 中间件名称，只用于内部传输，不进行远程传输
     */
    static public final String PRADAR_MIDDLEWARE_NAME = "p-pradar-middleware";


    /**
     * 上下文 调试ID 标
     */
    static public final String PRADAR_FAST_DEBUG_ID = PRADAR_HEADER_PREFIX + "pradar-fast-debug-id";


    /**
     * 上下文 的入口节点的 key
     */
    static public final String PRADAR_TRACE_NODE_KEY = "tn";

    /**
     * 上下文 节点 id
     */
    static public final String PRADAR_NODE_ID_KEY = "nid";

    private static IPradarService service;


    /**
     * 获取 pradar header 头前缀
     *
     * @return
     */
    public static String getPradarHeaderPrefix() {
        String value = System.getProperty(HEADER_PREFIX);
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * 注册pradar 服务
     *
     * @param pradarService pradar 服务
     */
    public static boolean registerPradarService(IPradarService pradarService) {
        service = pradarService;
        return true;
    }

    /**
     * 判断当前流量是否是压测流量
     *
     * @return 返回是否是压测流量
     */
    public static boolean isClusterTest() {
        if (service == null) {
            return false;
        }
        return service.isClusterTest();
    }

    /**
     * 向当前流量打标记
     *
     * @param key  标记key
     * @param mark 标记值
     */
    public static void mark(String key, String mark) {
        if (service == null) {
            return;
        }
        service.mark(key, mark);
    }

    /**
     * 当前流量取消标记
     *
     * @param key 标记key
     */
    public static void unmark(String key) {
        if (service == null) {
            return;
        }
        service.unmark(key);
    }

    /**
     * 判断当前流量是否有指定标记
     *
     * @param key 标记key
     * @return 返回当前流量是否有指定标记
     */
    public static boolean hasMark(String key) {
        if (service == null) {
            return false;
        }
        return service.hasMark(key);
    }


    /**
     * 获取调用上下文
     *
     * @return
     * @see #PRADAR_TRACE_ID_KEY
     * @see #PRADAR_INVOKE_ID_KEY
     * @see #PRADAR_DEBUG_KEY
     * @see #PRADAR_CLUSTER_TEST_KEY
     * @see #PRADAR_TRACE_ID_KEY
     * @see #PRADAR_NODE_ID_KEY
     * @see #PRADAR_LOG_TYPE_KEY
     * @see #PRADAR_TRACE_APPNAME_KEY
     * @see #PRADAR_REMOTE_APPNAME_KEY
     * @see #PRADAR_UPSTREAM_APPNAME_KEY
     * @see #PRADAR_REMOTE_IP
     * @see #PRADAR_START_TIME_KEY
     * @see #PRADAR_USER_DATA_KEY
     */
    public static Map<String, String> getInvokeContext() {
        if (service == null) {
            return Collections.EMPTY_MAP;
        }
        return service.getInvokeContext();
    }

    /**
     * 返回当前压测是否可用
     *
     * @return true|false
     */
    public static boolean isClusterTestEnabled() {
        if (service == null) {
            return false;
        }
        return service.isClusterTestEnabled();
    }
}
