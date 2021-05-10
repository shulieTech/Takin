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
import com.pamirs.pradar.Throwables;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author ranghai
 * @Date: 2020/9/8 09:38
 * @Description:
 */
public class RedisDataCheckInterceptor extends AroundInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doBefore(Advice advice) {
        Object[] args = advice.getParameterArray();

        if (!Pradar.isClusterTest()) {
            return;
        }

        if (RedisClientMediator.isShadowDb()) {
            return;
        }

        String methodName = advice.getBehavior().getName();

        if (RedisUtils.IGNORE_NAME.contains(methodName)) {
            return;
        }

        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();
        List<String> keys = null;

        if (RedisUtils.EVAL_METHOD_NAME.contains(methodName)) {
            keys = processEvalMethodName(args, whiteList);
        } else if (RedisUtils.METHOD_MORE_KEYS.containsKey(methodName)) {
            keys = processMoreKeys(methodName, whiteList, args);
        } else {
            keys = process(args, whiteList);
        }
        validateKeys(keys);
    }

    private void validateKeys(List<String> keys) {
        try {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                boolean isCluster = Pradar.isClusterTest();
                boolean withPt = Pradar.isClusterTestPrefix(key);
                if (isCluster && !withPt) {
                    throw new PressureMeasureError("jedis .压测流量进入业务库...,key = " + key);
                } else if (!isCluster && withPt) {
                    logger.error("jedis:业务流量进入压测库,key = {}", key);
                }
            }
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
    }

    private List<String> process(Object[] args, Collection<String> whiteList) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                return processKeyString(args, whiteList, i);
            } else if (args[i] instanceof String[]) {
                return processKeyStringArray(args, whiteList, i);
            } else if (args[i] instanceof byte[]) {
                return processKeyByte(args, whiteList, i);
            } else if (args[i] instanceof byte[][]) {
                return processKeyByteArray(args, whiteList, i);
            } else if (args[i] instanceof Map.Entry[]) {
                return processKeyMapEntry(args, whiteList, i);
            }
        }

        throw new PradarException("Jedis not support key deserialize !");
    }

    private String toKeyString(Object key) {
        if (key == null) {
            return null;
        }
        if (key instanceof String) {
            return (String) key;
        } else if (key instanceof byte[]) {
            try {
                return new String((byte[]) key, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String((byte[]) key);
            }
        } else {
            return key.toString();
        }
    }

    private List<String> processKeyMapEntry(Object[] args, Collection<String> whiteList, int keyIndex) {
        List<String> returnKeys = new ArrayList<String>();
        int keysIndex = keyIndex;
        Map.Entry[] keyBytes = (Map.Entry[]) args[keysIndex];

        for (int i = 0; i < keyBytes.length; i++) {
            String key = toKeyString(keyBytes[i].getKey());
            if (isNumeric(key)) {
                continue;
            }
            //白名单 忽略
            if (whiteListValidate(args, whiteList, key)) {
                continue;
            }
            returnKeys.add(key);
        }
        return returnKeys;
    }

    private List<String> processKeyByteArray(Object[] args, Collection<String> whiteList,
                                             int keyIndex) {
        List<String> returnKeys = new ArrayList<String>();
        int keysIndex = keyIndex;
        byte[][] keyBytes = (byte[][]) args[keysIndex];

        for (int i = 0; i < keyBytes.length; i++) {
            String key = new String(keyBytes[i]);

            if (isNumeric(key)) {
                continue;
            }
            //白名单 忽略
            if (whiteListValidate(args, whiteList, key)) {
                continue;
            }
            returnKeys.add(key);
        }
        return returnKeys;
    }

    private List<String> processKeyByte(Object[] args, Collection<String> whiteList, int keyIndex) {
        int keysIndex = keyIndex;
        String key = new String((byte[]) args[keysIndex]);
        //白名单 忽略
        if (whiteListValidate(args, whiteList, key)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(key);
    }

    private List<String> processKeyStringArray(Object[] args, Collection<String> whiteList,
                                               int keyIndex) {

        List<String> returnKeys = new ArrayList<String>();
        int keysIndex = keyIndex;
        String[] keys = (String[]) args[keysIndex];
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (isNumeric(key)) {
                continue;
            }
            //白名单 忽略
            if (whiteListValidate(args, whiteList, key)) {
                continue;
            }
            returnKeys.add(key);
        }
        return returnKeys;
    }

    public boolean isNumeric(String key) {
        boolean numeric = StringUtils.isNumeric(key);
        if (numeric) {
            return true;
        }
        return false;
    }

    private List<String> processMoreKeys(String methodName, Collection<String> whiteList, Object[] args) {
        List<Integer> keyIndexes = RedisUtils.METHOD_MORE_KEYS.get(methodName);
        //如果出现枚举的值比方法参数数量大的，则进行判断单个key逻辑处理
        for (int i = 0; i < keyIndexes.size(); i++) {
            if (args.length < (keyIndexes.get(i) + 1)) {
                return process(args, whiteList);
            }
        }

        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < keyIndexes.size(); i++) {
            List<String> strings = processIndex(args, whiteList, keyIndexes.get(i));
            keys.addAll(strings);
        }

        return keys;
    }

    private List<String> processIndex(Object[] args, Collection<String> whiteList, int keyIndex) {
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

    private List<String> processKeyString(Object[] args, Collection<String> whiteList, int keyIndex) {
        String key = (String) args[keyIndex];
        if (whiteListValidate(args, whiteList, key)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(key);
    }

    private boolean whiteListValidate(Object[] args, Collection<String> whiteList, String key) {
        for (String white : whiteList) {
            if (key.startsWith(white)) {
                return true;
            }
        }
        return false;
    }

    private List<String> processEvalMethodName(Object[] args, Collection<String> whiteList) {

        List<String> keys = new ArrayList<String>();
        if (args.length != 3) {
            return keys;
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
                    keys.add(key);
                }
            }
        } else if (args[1] instanceof byte[]) {
            int keyCount = 0;
            try {
                keyCount = Integer.valueOf(new String((byte[]) args[1]));
            } catch (NumberFormatException e) {
                return keys;
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
                    keys.add(key);
                }
            }
        } else if (args[1] instanceof java.util.List) {
            List<Object> list = (List<Object>) args[1];
            List<Object> ptList = new ArrayList<Object>();

            for (Object o : list) {
                String key;
                if (o instanceof String) {
                    key = (String) o;
                    keys.add(key);
                } else if (o instanceof byte[]) {
                    key = new String((byte[]) o);
                    keys.add(key);
                } else {
                    throw new PradarException("redis lua not support type " + o.getClass().getName());
                }
            }
        }
        return keys;
    }

}
