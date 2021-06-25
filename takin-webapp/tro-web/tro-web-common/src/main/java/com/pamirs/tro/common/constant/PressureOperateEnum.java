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

package com.pamirs.tro.common.constant;

/**
 * 说明: 压测启动,停止,暂停枚举类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/7/2 18:22
 */
public enum PressureOperateEnum {

    /**
     * 说明：启动压测
     */
    PRESSURE_START,

    /**
     * 说明：停止压测
     */
    PRESSURE_STOP,

    /**
     * 说明：暂停压测
     */
    PRESSURE_PAUSE,

    /**
     * 说明：数据构建调试开关
     */
    DATA_BUILD_DEBUG_SWITCH,

    /**
     * 说明：压测检测调试开关
     */
    PRESSURE_CHECK_DEBUG_SWITCH,
    ;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
