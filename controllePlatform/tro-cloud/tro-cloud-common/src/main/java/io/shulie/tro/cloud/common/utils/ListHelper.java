/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName ListHelper
 * @Description 工具类
 * @Author qianshui
 * @Date 2019-06-21 15:40
 */
public class ListHelper {

    public static <T, K, V> Map<K, V> transferToMap(List<T> list, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if(CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_MAP;
        }
        Map<K, V> result = Maps.newLinkedHashMap();
        for (T t : list) {
            K key = keyFunc.apply(t);
            V value = valueFunc.apply(t);
            result.put(key, value);
        }
        return result;
    }

    public static <T, K, V> Map<K, List<V>> transferToListMap(List<T> list, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        Map<K, List<V>> result = Maps.newLinkedHashMap();
        for (T t : list) {
            K key = keyFunc.apply(t);
            V value = valueFunc.apply(t);
            List<V> values = result.get(key);
            if (values == null) {
                values = Lists.newArrayList();
                result.put(key, values);
            }
            values.add(value);
        }
        return result;
    }

    /**
     * @return java.util.List
     * @Author ZhangXT
     * @Description copyList
     * @Date 2019/6/11 20:13
     * @Param [list, tClass]
     */
    public static <T> List copyList(List<T> list, Class tClass) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JSON.parseArray(JSON.toJSONString(list), tClass);
    }

    public static String[] listToArray(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new String[]{};
        }
        return list.toArray(new String[list.size()]);
    }
}
