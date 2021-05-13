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

package io.shulie.tro.cloud.common.constants;

/**
 * @author zhaoyong
 */
public class PressureInstanceRedisKey {

    /**
     * 一级redis key
     */
    private static final String PRESSURE_ENGINE_INSTANCE_REDIS_KEY = "PRESSURE:ENGINE:INSTANCE:%s:%s:%s";


    /**
     * 二级redis key
     */
    public static class SecondRedisKey{

        public static final String CONFIG_ID = "CONFIG_MAP_ID";

        public static final String CONFIG_NAME = "CONFIG_MAP_NAME";

        public static final String APP_ID = "APP_ID";

        public static final String REDIS_TPS_LIMIT = "REDIS_TPS_LIMIT";

        public static final String REDIS_TPS_ALL_LIMIT = "REDIS_TPS_ALL_LIMIT";

        public static final String REDIS_TPS_POD_NUM = "REDIS_TPS_POD_NUM";
    }


    public static String getEngineInstanceRedisKey(Long sceneId, Long taskId, Long customerId){
        return String.format(PressureInstanceRedisKey.PRESSURE_ENGINE_INSTANCE_REDIS_KEY, sceneId, taskId, customerId);
    }
}
