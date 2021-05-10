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
package com.pamirs.attach.plugin.jedis.interceptor;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.attach.plugin.jedis.util.RedisUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class MJedisInterceptor extends ParametersWrapperInterceptorAdaptor {

    @Override
    public Object[] getParameter0(Advice advice) {

        ClusterTestUtils.validateClusterTest();

        Object[] args = advice.getParameterArray();
        if (args == null || args.length == 0) {
            return args;
        }

        if (!Pradar.isClusterTest()) {
            return args;
        }

        if (RedisClientMediator.isShadowDb()) {
            return args;
        }

        String methodName = advice.getBehavior().getName();

        if (RedisUtils.IGNORE_NAME.contains(methodName)) {
            return args;
        }

        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();

        if (RedisUtils.EVAL_METHOD_NAME.contains(methodName)) {
            return processEvalMethodName(args, whiteList);
        }

        //String methodSign = getMethodSign(methodName,args);
        if (RedisUtils.METHOD_MORE_KEYS.containsKey(methodName)) {
            return processMoreKeys(methodName, args, whiteList);
        }

        //jedis db非0时候选择不做处理
        if ("select".equals(advice.getBehavior().getName())) {
            return args;
        }

        if ("xread".equals(advice.getBehavior().getName())) {
            return processXRead(args, whiteList);
        }

        if ("xreadGroup".equals(advice.getBehavior().getName())) {
            return processXReadGroup(args, whiteList);
        }

        return process(args, whiteList);
    }

    private Object[] processXRead(Object[] args, Collection<String> whiteList) {
        if (args.length != 3) {
            return args;
        }
        Object arg2 = args[2];
        if (!(arg2 instanceof Map.Entry[])) {
            return args;
        }

        Map.Entry[] entries = (Map.Entry[]) arg2;
        Map.Entry[] newEntries = new Map.Entry[entries.length];
        for (int i = 0, len = entries.length; i < len; i++) {
            final Map.Entry entry = entries[i];
            final Object clusterKey = toClusterTestKey(entry.getKey());
            if (ObjectUtils.equals(clusterKey, entry.getKey())) {
                newEntries[i] = entry;
            } else {
                newEntries[i] = new Map.Entry() {
                    private Object key = clusterKey;
                    private Object value = entry.getValue();

                    @Override
                    public Object getKey() {
                        return key;
                    }

                    @Override
                    public Object getValue() {
                        return value;
                    }

                    @Override
                    public Object setValue(Object value) {
                        Object oldValue = this.value;
                        this.value = value;
                        return oldValue;
                    }
                };
            }
        }
        args[2] = newEntries;
        return args;
    }

    private Object[] processXReadGroup(Object[] args, Collection<String> whiteList) {
        if (args.length != 6) {
            return args;
        }
        Object arg5 = args[5];
        if (!(arg5 instanceof Map.Entry[])) {
            return args;
        }

        Map.Entry[] entries = (Map.Entry[]) arg5;
        Map.Entry[] newEntries = new Map.Entry[entries.length];
        for (int i = 0, len = entries.length; i < len; i++) {
            final Map.Entry entry = entries[i];
            final Object clusterKey = toClusterTestKey(entry.getKey());
            if (ObjectUtils.equals(clusterKey, entry.getKey())) {
                newEntries[i] = entry;
            } else {
                newEntries[i] = new Map.Entry() {
                    private Object key = clusterKey;
                    private Object value = entry.getValue();

                    @Override
                    public Object getKey() {
                        return key;
                    }

                    @Override
                    public Object getValue() {
                        return value;
                    }

                    @Override
                    public Object setValue(Object value) {
                        Object oldValue = this.value;
                        this.value = value;
                        return oldValue;
                    }
                };
            }
        }
        args[5] = newEntries;
        return args;
    }

    private Object[] process(Object[] args, Collection<String> whiteList) {
        //遍历顺序获取一下几个类型值
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                return processKeyString(args, whiteList, i);
            } else if (args[i] instanceof String[]) {
                return processKeyStringArray(args, whiteList, i);
            } else if (args[i] instanceof byte[]) {
                return processKeyByte(args, whiteList, i);
            } else if (args[i] instanceof byte[][]) {
                return processKeyByteArray(args, whiteList, i);
            }
        }

        throw new PradarException("Jedis not support key deserialize !");
    }

    private Object[] processIndex(Object[] args, Collection<String> whiteList, int keyIndex) {
        if (args[keyIndex] instanceof String) {
            return processKeyString(args, whiteList, keyIndex);
        } else if (args[keyIndex] instanceof String[]) {
            return processKeyStringArray(args, whiteList, keyIndex);
        } else if (args[keyIndex] instanceof byte[]) {
            return processKeyByte(args, whiteList, keyIndex);
        } else if (args[keyIndex] instanceof byte[][]) {
            return processKeyByteArray(args, whiteList, keyIndex);
        } else {
            throw new PradarException("Jedis not support key deserialize !");
        }
    }

    private Object[] processKeyStringArray(Object[] args, Collection<String> whiteList, int keyIndex) {
        int keysIndex = keyIndex;
        String[] keys = (String[]) args[keysIndex];
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];

            if (isNumeric(key)) {
                continue;
            }

            //白名单 忽略
            if (whiteListValidate(whiteList, key)) {
                continue;
            }
            if (!Pradar.isClusterTestPrefix(key)) {
                key = Pradar.addClusterTestPrefix(key);
                keys[i] = key;
            }
        }

        args[keysIndex] = keys;
        return args;
    }

    public boolean isNumeric(String key) {
        boolean numeric = StringUtils.isNumeric(key);
        if (numeric) {
            return true;
        }
        return false;
    }

    private Object[] processMoreKeys(String methodName, Object[] args, Collection<String> whiteList) {
        List<Integer> keyIndexes = RedisUtils.METHOD_MORE_KEYS.get(methodName);
        //如果出现枚举的值比方法参数数量大的，则进行判断单个key逻辑处理
        for (int i = 0; i < keyIndexes.size(); i++) {
            if (args.length < (keyIndexes.get(i) + 1)) {
                return process(args, whiteList);
            }
        }

        for (int i = 0; i < keyIndexes.size(); i++) {
            processIndex(args, whiteList, keyIndexes.get(i));
        }
        return args;
    }

    private Object[] processKeyString(Object[] args, Collection<String> whiteList, int keyIndex) {
        String key = (String) args[keyIndex];

        if (whiteListValidate(whiteList, key)) {
            return args;
        }

        if (!Pradar.isClusterTestPrefix(key)) {
            key = Pradar.addClusterTestPrefix(key);
            args[keyIndex] = key;
            return args;
        }
        return args;
    }

    private boolean whiteListValidate(Collection<String> whiteList, String key) {
        for (String white : whiteList) {
            if (key.startsWith(white)) {
                return true;
            }
        }
        return false;
    }

    private Object[] processEvalMethodName(Object[] args, Collection<String> whiteList) {
        if (args.length != 3) {
            return args;
        }

        if (args[1] instanceof Integer) {
            int keyCount = (Integer) args[1];
            Object[] params = (Object[]) args[2];
            if (keyCount <= params.length) {
                for (int i = 0; i < keyCount; i++) {
                    Object data = params[i];
                    String key;
                    if (data instanceof String) {
                        key = (String) data;
                    } else if (data instanceof byte[]) {
                        key = new String((byte[]) data);
                    } else {
                        throw new PradarException("redis lua not support type " + data.getClass().getName());
                    }
                    if (RedisUtils.IGNORE_NAME.contains(key)) {
                        continue;
                    }
                    boolean contains = false;
                    for (String white : whiteList) {
                        if (key.startsWith(white)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        continue;
                    }
                    if (Pradar.isClusterTestPrefix(key)) {
                        continue;
                    }
                    if (params[i] instanceof String) {
                        params[i] = Pradar.addClusterTestPrefix(key);
                    } else {
                        params[i] = (Pradar.addClusterTestPrefix(key)).getBytes();
                    }
                }
            }
        } else if (args[1] instanceof byte[]) {
            int keyCount = 0;
            try {
                keyCount = Integer.valueOf(new String((byte[]) args[1]));
            } catch (NumberFormatException e) {
                return args;
            }
            byte[][] params = (byte[][]) args[2];
            if (keyCount <= params.length) {
                for (int i = 0; i < keyCount; i++) {
                    byte[] data = params[i];
                    String key = new String((byte[]) data);
                    if (RedisUtils.IGNORE_NAME.contains(key)) {
                        continue;
                    }
                    boolean contains = false;
                    for (String white : whiteList) {
                        if (key.startsWith(white)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        continue;
                    }
                    if (Pradar.isClusterTestPrefix(key)) {
                        continue;
                    }
                    params[i] = (Pradar.addClusterTestPrefix(key)).getBytes();
                }
            }
        } else if (args[1] instanceof java.util.List) {
            List<Object> list = (List<Object>) args[1];
            List<Object> ptList = new ArrayList<Object>();

            for (Object o : list) {
                String key;
                if (o instanceof String) {
                    key = (String) o;
                    if (!key.startsWith(Pradar.CLUSTER_TEST_PREFIX)) {
                        ptList.add((Pradar.addClusterTestPrefix(key)));
                    } else {
                        ptList.add(key);
                    }
                } else if (o instanceof byte[]) {
                    key = new String((byte[]) o);
                    if (!Pradar.isClusterTestPrefix(key)) {
                        ptList.add((Pradar.addClusterTestPrefix(key)).getBytes());
                    } else {
                        ptList.add(key.getBytes());
                    }
                } else {
                    throw new PradarException("redis lua not support type " + o.getClass().getName());
                }
            }
            args[1] = ptList;
        }
        return args;
    }

    private Object[] processKeyByte(Object[] args, Collection<String> whiteList, int keyIndex) {
        int keysIndex = keyIndex;
        String key = new String((byte[]) args[keysIndex]);

        //白名单 忽略
        if (ignore(whiteList, key)) {
            return args;
        }

        if (!Pradar.isClusterTestPrefix(key)) {
            key = Pradar.addClusterTestPrefix(key);
            args[keysIndex] = key.getBytes();
            return args;
        }

        return args;
    }

    private Object[] processKeyByteArray(Object[] args, Collection<String> whiteList, int keyIndex) {
        int keysIndex = keyIndex;
        byte[][] keyBytes = (byte[][]) args[keysIndex];

        for (int i = 0; i < keyBytes.length; i++) {
            String key = new String(keyBytes[i]);

            if (isNumeric(key)) {
                continue;
            }

            //白名单 忽略
            if (whiteListValidate(whiteList, key)) {
                continue;
            }
            if (!Pradar.isClusterTestPrefix(key)) {
                key = Pradar.addClusterTestPrefix(key);
                keyBytes[i] = key.getBytes();
            }
        }

        args[keysIndex] = keyBytes;
        return args;
    }

    private Object toClusterTestKey(Object key) {
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
