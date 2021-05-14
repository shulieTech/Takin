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

package io.shulie.tro.web.app.utils;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @Author: fanxx
 * @Date: 2020/11/7 下午3:33
 * @Description: map排序工具类
 */
public class MapOrderByUtils {
    /**
     * @Description: 根据map的key排序
     * @Author danrenying
     * @Param: [map, isDesc: true：降序，false：升序]
     * @Return: java.util.Map<K, V>
     * @Date 2020/7/13
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> orderByKey(Map<K, V> map, boolean isDesc) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

}
