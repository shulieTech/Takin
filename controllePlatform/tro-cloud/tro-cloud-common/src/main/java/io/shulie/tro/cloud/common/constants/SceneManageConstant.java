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
 * @ClassName PtConfigConstant
 * @Description
 * @Author qianshui
 * @Date 2020/4/18 上午11:49
 */
public class SceneManageConstant {

    public static final String FILE_SPLIT = "/";

    public static final String SCENE_TOPIC_PREFIX = "TRO_SCENE_";

    // ---- 施压配置
    public static final String THREAD_NUM = "threadNum";

    public static final String HOST_NUM = "hostNum";

    public static final String PT_DURATION = "ptDuration";

    public static final String PT_DURATION_UNIT = "ptDurationUnit";

    /**
     * 0:并发,1:tps,2:自定义
     */
    public static final String PT_TYPE = "ptType";

    public static final String PT_MODE = "ptMode";

    public static final String STEP_DURATION = "stepDuration";

    public static final String STEP_DURATION_UNIT = "stepDurationUnit";

    public static final String STEP = "step";

    public static final String ESTIMATE_FLOW = "estimateFlow";

    public static final String DATA_COUNT = "dataCount";

    public static final String IS_SPLIT = "isSplit";

    public static final String TOPIC = "topic";

    // ---- 业务活动目标
    public static final String TPS= "TPS";

    public static final String RT = "RT";

    public static final String SUCCESS_RATE = "successRate";

    public static final String SA = "SA";

    // ---- SLA配置
    public static final String COMPARE_TYPE= "compareType";

    public static final String COMPARE_VALUE = "compareValue";

    public static final String ACHIEVE_TIMES = "achieveTimes";

    public static final String EVENT = "event";

    public static final String EVENT_WARN = "warn";

    public static final String EVENT_DESTORY = "destory";

    //features ,扩展字段
    public static final String FEATURES_CONFIG_TYPE = "configType" ;

    public static final String FEATURES_SCRIPT_ID = "scriptId" ;

    public static final String FEATURES_BUSINESS_FLOW_ID ="businessFlowId";

    public static final String FEATURES_SCHEDULE_INTERVAL = "scheduleInterval";

    //压测场景模拟业务流量启动场景
    public static final String SCENE_MANAGER_FLOW_DEBUG = "SCENE_MANAGER_FLOW_DEBUG_";
}
