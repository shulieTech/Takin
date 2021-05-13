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

package io.shulie.tro.web.data.result.fastdebug;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-30 21:29
 * @Description:
 */

@Data
public class FastDebugMachinePerformanceResult implements Serializable {

    private static final long serialVersionUID = 8872344698198155835L;

    private Long id;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpcid
     */
    private String rpcId;

    /**
     * 性能类型，beforeFirst/beforeLast/afterFirst/afterLast/exceptionFirst/exceptionLast
     */
    private String index;

    /**
     * cpu利用率
     */
    private BigDecimal cpuUsage;

    /**
     * cpu load
     */
    private BigDecimal cpuLoad;

    /**
     * 没存利用率
     */
    private BigDecimal memoryUsage;

    /**
     * 堆内存总和
     */
    private BigDecimal memoryTotal;

    /**
     * io 等待率
     */
    private BigDecimal ioWait;

    /**
     * younggc 次数
     */
    private Long youngGcCount;

    /**
     * young gc 耗时
     */
    private Long youngGcTime;

    /**
     * old gc 次数
     */
    private Long oldGcCount;

    /**
     * old gc 耗时
     */
    private Long oldGcTime;


    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;

}
