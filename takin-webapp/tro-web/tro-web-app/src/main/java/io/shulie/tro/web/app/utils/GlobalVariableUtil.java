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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pamirs.tro.common.constant.TRODictTypeEnum;

/**
 * 全局变量存放地
 *
 * @author shulie
 * @description
 * @create 2018-08-18 08:50:15
 */
public class GlobalVariableUtil {

    public static ConcurrentMap<String, Object> createCacheMap() {
        return InnerConcurrentMap.cacheMap;
    }

    public static void setValue(TRODictTypeEnum troDictTypeEnum, Map<String, Object> map) {
        InnerConcurrentMap.cacheMap.put(troDictTypeEnum.toString(), map);
    }

    public static Map<String, Object> getValue(TRODictTypeEnum troDictTypeEnum) {
        return (Map<String, Object>)InnerConcurrentMap.cacheMap.get(troDictTypeEnum.toString());
    }

    public static class InnerConcurrentMap {
        public static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();
    }
}
