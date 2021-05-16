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
package com.pamirs.attach.plugin.apache.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 7:23 下午
 */
public final class ConfigCache {
    private static Map<Integer, String> servers = new HashMap<Integer, String>();
    private static Map<Integer, String> groups = new HashMap<Integer, String>();
    private static ConcurrentMap<Integer, Boolean> isInited = new ConcurrentHashMap<Integer, Boolean>();

    public static void clear() {
        isInited.clear();
    }

    public static boolean isInited(Object target) {
        int code = System.identityHashCode(target);
        Boolean old = isInited.putIfAbsent(code, Boolean.TRUE);
        if (old != null) {
            return true;
        }
        return false;
    }


    public static void setServers(Object target, String server) {
        servers.put(System.identityHashCode(target), server);
    }

    public static String getServers(Object target) {
        return servers.get(System.identityHashCode(target));
    }

    public static void setGroup(Object target, String group) {
        groups.put(System.identityHashCode(target), group);
    }

    public static String getGroup(Object target) {
        return groups.get(System.identityHashCode(target));
    }
}
