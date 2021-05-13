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

package io.shulie.tro.web.data.param.perfomanceanaly;

import lombok.Data;


/**
 * @Author: mubai
 * @Date: 2020-11-13 11:39
 * @Description:
 */

@Data
public class PressureMachineStatisticsInsertParam  {

    /**
     * 总数量
     */
    private Integer machineTotal;

    /**
     * 压测中数量
     */
    private Integer machinePressured;

    /**
     * 空闲机器数量
     */
    private Integer machineFree;

    /**
     * 离线机器数量
     */
    private Integer machineOffline;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDeleted;

}
