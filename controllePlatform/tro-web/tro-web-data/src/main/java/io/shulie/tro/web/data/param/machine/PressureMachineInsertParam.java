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

package io.shulie.tro.web.data.param.machine;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-12 20:57
 * @Description:
 */

@Data
public class PressureMachineInsertParam {

    /**
     * id
     */
    private Long id;

    /**
     * 压力机名称
     */
    private String name;

    /**
     * 压力机IP
     */
    private String ip;

    /**
     * 标签
     */
    private String flag;

    /**
     * cpu核数
     */
    private Integer cpu;

    /**
     * 内存，单位字节
     */
    private Long memory;

    /**
     * 磁盘，单位字节
     */
    private Long disk;

    private BigDecimal machineUsage;

    /**
     * cpu利用率
     */
    private BigDecimal cpuUsage;

    /**
     * cpu load
     */
    private BigDecimal cpuLoad;

    /**
     * 内存利用率
     */
    private BigDecimal memoryUsed;

    /**
     * 磁盘 IO 等待率
     */
    private BigDecimal diskIoWait;


    /**
     * 网络带宽总大小
     */
    private Long transmittedTotal;

    /**
     * 网络带宽入大小
     */
    private Long transmittedIn;

    /**
     * 网络带宽入利用率
     */
    private BigDecimal transmittedInUsage;

    /**
     * 网络带宽出大小
     */
    private Long transmittedOut;

    /**
     * 网络带宽出利用率
     */
    private BigDecimal transmittedOutUsage;

    /**
     * 网络带宽利用率
     */
    private BigDecimal transmittedUsage;

    /**
     * 压测场景id
     */
    private String sceneNames;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    private Integer status;

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

}


