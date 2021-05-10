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
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;

import java.util.*;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/8 4:06 下午
 */
public abstract class ClusterTestArgsWrapperInterceptor extends ParametersWrapperInterceptorAdaptor {
    protected Object toClusterTestKey(Object key) {
        if (key == null) {
            return null;
        }
        if (key instanceof byte[]) {
            return getBytes((byte[]) key);
        }

        if (key instanceof byte[][]) {
            return getBytesArray((byte[][]) key);
        }

        if (key instanceof char[]) {
            return getChars((char[]) key);
        }

        if (key instanceof char[][]) {
            return getCharsArray((char[][]) key);
        }

        if (key instanceof String) {
            return getString((String) key);
        }

        if (key instanceof List) {
            List list = new ArrayList();
            for (Object k : (List) key) {
                list.add(toClusterTestKey(k));
            }
            return list;
        }

        if (key instanceof Set) {
            Set set = new HashSet();
            for (Object k : (Set) key) {
                set.add(toClusterTestKey(k));
            }
            return set;
        }

        if (key instanceof Iterable) {
            List list = new ArrayList();
            Iterator it = ((Iterable) key).iterator();
            while (it.hasNext()) {
                Object k = it.next();
                list.add(toClusterTestKey(k));
            }
            return list;
        }

        if (key instanceof Map) {
            Map map = (Map) key;
            Map result = new HashMap();

            final Set<Map.Entry<Object, Object>> set = map.entrySet();
            for (Map.Entry<Object, Object> entry : set) {
                Object k = entry.getKey();
                result.put(toClusterTestKey(k), entry.getValue());
            }
            return result;
        }

        if (key instanceof Iterator) {
            List list = new ArrayList();
            Iterator it = (Iterator) key;
            while (it.hasNext()) {
                Object k = it.next();
                list.add(toClusterTestKey(k));
            }
            return list;
        }

        if (key instanceof Object[]) {
            Object[] keys = (Object[]) key;
            for (int i = 0, len = keys.length; i < len; i++) {
                Object k = toClusterTestKey(keys[i]);
                keys[i] = k;
            }
            return keys;
        }

        String str = key.toString();
        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        if (ignore(whiteList, str)) {
            return str;
        }
        if (!Pradar.isClusterTestPrefix(str)) {
            str = Pradar.addClusterTestPrefix(str);
        }
        return str;

    }

    private String getString(String key) {
        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        if (ignore(whiteList, key)) {
            return key;
        }
        String str = key;
        if (!Pradar.isClusterTestPrefix(str)) {
            str = Pradar.addClusterTestPrefix(str);
        }
        return str;
    }

    private char[][] getCharsArray(char[][] key) {
        char[][] datas = key;
        for (int i = 0, len = datas.length; i < len; i++) {
            String str = new String(datas[i]);
            Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
            if (ignore(whiteList, str)) {
                continue;
            }
            if (!Pradar.isClusterTestPrefix(str)) {
                str = Pradar.addClusterTestPrefix(str);
            }
            datas[i] = str.toCharArray();
        }
        return datas;
    }

    private char[] getChars(char[] key) {
        String str = new String(key);
        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        if (ignore(whiteList, str)) {
            return key;
        }
        if (!Pradar.isClusterTestPrefix(str)) {
            str = Pradar.addClusterTestPrefix(str);
        }
        return str.toCharArray();
    }

    private byte[][] getBytesArray(byte[][] key) {
        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        byte[][] datas = key;
        for (int i = 0, len = datas.length; i < len; i++) {
            String str = new String(datas[i]);
            if (ignore(whiteList, str)) {
                continue;
            }
            if (!Pradar.isClusterTestPrefix(str)) {
                str = Pradar.addClusterTestPrefix(str);
            }
            datas[i] = str.getBytes();
        }
        return datas;
    }

    private boolean ignore(Collection<String> whiteList, String key) {
        //白名单 忽略
        for (String white : whiteList) {
            if (key.startsWith(white)) {
                return true;
            }
        }
        return false;
    }

    private byte[] getBytes(byte[] key) {
        String str = new String(key);

        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        if (ignore(whiteList, str)) {
            return key;
        }

        if (!Pradar.isClusterTestPrefix(str)) {
            str = Pradar.addClusterTestPrefix(str);
        }
        return str.getBytes();
    }
}
