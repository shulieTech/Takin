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
package com.shulie.instrument.simulator.api.util;

import java.util.Map;

/**
 * 参数工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/28 7:48 下午
 */
public final class ParameterUtils {

    /**
     * 从 map 中获取 key 对应的值
     *
     * @param param map
     * @param key   键
     * @return
     */
    public static String getProperty(final Map<String, String> param, String key) {
        return getProperty(param, key, null);
    }

    /**
     * 从 map 中获取 key 对应的值，如果不存在则返回 defaultValue
     *
     * @param param        map
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public static String getProperty(final Map<String, String> param, String key, String defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 解析出Byte
     *
     * @param param 参数
     * @param key   键
     * @return Byte
     */
    public static Byte getByte(final Map<String, String> param, String key) {
        return getByte(param, key, null);
    }

    /**
     * 解析出Byte
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Byte
     */
    public static Byte getByte(final Map<String, String> param, String key, Byte defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Byte.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Short
     *
     * @param param 参数
     * @param key   键
     * @return Short
     */
    public static Short getShort(final Map<String, String> param, String key) {
        return getShort(param, key, null);
    }

    /**
     * 解析出Short
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Short
     */
    public static Short getShort(final Map<String, String> param, String key, Short defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Short.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Integer
     *
     * @param param 参数
     * @param key   键
     * @return Integer
     */
    public static Integer getInt(final Map<String, String> param, String key) {
        return getInt(param, key, null);
    }

    /**
     * 解析出Integer
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer getInt(final Map<String, String> param, String key, Integer defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Long
     *
     * @param param 参数
     * @param key   键
     * @return Long
     */
    public static Long getLong(final Map<String, String> param, String key) {
        return getLong(param, key, null);
    }

    /**
     * 解析出Long
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Long
     */
    public static Long getLong(final Map<String, String> param, String key, Long defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Float
     *
     * @param param 参数
     * @param key   键
     * @return Float
     */
    public static Float getFloat(final Map<String, String> param, String key) {
        return getFloat(param, key, null);
    }

    /**
     * 解析出Float
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Float
     */
    public static Float getFloat(final Map<String, String> param, String key, Float defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Double
     *
     * @param param 参数
     * @param key   键
     * @return Double
     */
    public static Double getDouble(final Map<String, String> param, String key) {
        return getDouble(param, key, null);
    }

    /**
     * 解析出Double
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Double
     */
    public static Double getDouble(final Map<String, String> param, String key, Double defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析出Boolean
     *
     * @param param 参数
     * @param key   键
     * @return Boolean
     */
    public static Boolean getBoolean(final Map<String, String> param, String key) {
        return getBoolean(param, key, null);
    }

    /**
     * 解析出Boolean
     *
     * @param param        参数
     * @param key          键
     * @param defaultValue 默认值
     * @return Boolean
     */
    public static Boolean getBoolean(final Map<String, String> param, String key, Boolean defaultValue) {
        String value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Boolean.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
