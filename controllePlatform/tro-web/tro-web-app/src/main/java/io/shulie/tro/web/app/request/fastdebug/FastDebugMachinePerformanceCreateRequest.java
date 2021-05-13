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

package io.shulie.tro.web.app.request.fastdebug;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-28 10:50
 * @Description:
 */

@Data
public class FastDebugMachinePerformanceCreateRequest implements Serializable {
    private static final long serialVersionUID = -2346446507538438373L;

    /**
     * 类型： beforeFirst/beforeLast/afterFirst/afterLast/exceptionFirst/exceptionLast
     */
    private String index;

    /**
     * cpu 利用率
     */
    private String cpuUsage;

    /**
     * cpu load
     */
    private String cpuLoad;

    /**
     * 内存利用率
     */
    private String memoryUsage;

    /**
     * 堆内存占用总和
     */
    private String memoryTotal;

    /**
     * ioWait
     */
    private String ioWait;

    /**
     * younggc 次数
     */
    private long youngGcCount;

    /**
     * young gc 耗时
     */
    private long youngGcTime;

    /**
     * old gc 次数
     */
    private long oldGcCount;

    /**
     * old gc 耗时
     */
    private long oldGcTime;

}

