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
package com.shulie.instrument.simulator.core.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin.zfb
 * @since 2020/9/18 2:33 下午
 */
public final class CastUtils {

    private static Map<Class, List<Class>> primitiveWrappers = new HashMap<Class, List<Class>>();
    private static Map<Class, List<Class>> wrapperPrimitives = new HashMap<Class, List<Class>>();

    static {
        primitiveWrappers.put(byte.class, asList(Byte.class, char.class));
        wrapperPrimitives.put(Byte.class, asList(byte.class));

        primitiveWrappers.put(short.class, asList(Short.class, byte.class));
        wrapperPrimitives.put(Short.class, asList(short.class));

        primitiveWrappers.put(int.class, asList(Integer.class, byte.class, short.class));
        wrapperPrimitives.put(Integer.class, asList(int.class));

        primitiveWrappers.put(long.class, asList(Long.class, byte.class, short.class, int.class));
        wrapperPrimitives.put(Long.class, asList(long.class));

        primitiveWrappers.put(float.class, asList(Float.class));
        wrapperPrimitives.put(Float.class, asList(float.class));

        primitiveWrappers.put(double.class, asList(Double.class, float.class));
        wrapperPrimitives.put(Double.class, asList(double.class));

        primitiveWrappers.put(char.class, asList(Character.class, byte.class));
        wrapperPrimitives.put(Character.class, asList(char.class));

        primitiveWrappers.put(boolean.class, asList(Boolean.class));
        wrapperPrimitives.put(Boolean.class, asList(boolean.class));
    }

    private static List<Class> asList(Class... classes) {
        return Arrays.asList(classes);
    }

    public static boolean canAssign(Object object, Class type) {
        if (object == null) {
            if (type.isPrimitive()) {
                return false;
            }
            return true;
        }
        if (object.getClass() == type) {
            return true;
        }

        if (type.isAssignableFrom(object.getClass())) {
            return true;
        }

        if (type.isPrimitive()) {
            List<Class> list = primitiveWrappers.get(type);
            return list.contains(object.getClass());
        }

        if (object.getClass().isPrimitive()) {
            List<Class> list = wrapperPrimitives.get(type);
            return list.contains(object.getClass());
        }

        return false;
    }
}
