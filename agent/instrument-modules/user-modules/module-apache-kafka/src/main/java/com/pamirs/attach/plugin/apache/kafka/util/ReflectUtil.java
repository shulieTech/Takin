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
package com.pamirs.attach.plugin.apache.kafka.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.util
 * @Date 2020-04-10 18:43
 */
public class ReflectUtil {

    private static final Map<String, Field> FIELDMAP = new ConcurrentHashMap<String, Field>();
    private static final Map<String, Method> METHODMAP = new ConcurrentHashMap<String, Method>();

    public static Field getField(Object o, String fieldName) throws Exception {
        String key = o.toString() + "-" + fieldName;
        Field field = FIELDMAP.get(key);
        if (null == field) {
            if (o instanceof Class) {
                Class clazz = (Class) o;
                field = clazz.getDeclaredField(fieldName);
            } else {
                Class clazz = o.getClass();
                field = clazz.getDeclaredField(fieldName);
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            FIELDMAP.put(key, field);
        }
        return field;
    }

    public static Method getMethod(Object o, String fieldName, Class... pamras) throws Exception {
        String key = o.toString() + "-" + fieldName;
        Method method = METHODMAP.get(key);
        if (null == method) {
            if (o instanceof Class) {
                Class clazz = (Class) o;
                method = clazz.getDeclaredMethod(fieldName, pamras);
            } else {
                Class clazz = o.getClass();
                method = clazz.getDeclaredMethod(fieldName, pamras);
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            METHODMAP.put(key, method);
        }
        return method;
    }

    public static <T> T reflectSlience(Object obj, String name) {
        try {
            return Reflect.on(obj).get(name);
        } catch (ReflectException ignore) {
            return null;
        }
    }
}
