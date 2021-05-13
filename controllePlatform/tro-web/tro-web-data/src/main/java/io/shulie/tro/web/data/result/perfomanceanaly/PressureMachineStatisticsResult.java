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

package io.shulie.tro.web.data.result.perfomanceanaly;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-11-13 11:51
 * @Description:
 */

@Data
public class PressureMachineStatisticsResult {

    /**
     * id
     */
    private Long id;

    /**
     * 总数量
     */
    @JsonProperty("machine_total")
    private Integer machineTotal = 0;

    /**
     * 压测中数量
     */
    @JsonProperty("machine_pressured")
    private Integer machinePressured = 0;

    /**
     * 空闲机器数量
     */
    @JsonProperty("machine_free")
    private Integer machineFree = 0;

    /**
     * 离线机器数量
     */
    @JsonProperty("machine_offline")
    private Integer machineOffline = 0;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtUpdate;

    private Long time ;
}
