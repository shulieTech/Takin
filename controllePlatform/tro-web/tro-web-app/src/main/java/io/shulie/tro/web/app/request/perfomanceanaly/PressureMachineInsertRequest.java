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

package io.shulie.tro.web.app.request.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-13 09:03
 * @Description:
 */

@Data
@ApiModel(value = "压力机上报请求模型")
public class PressureMachineInsertRequest implements Serializable {

    /**
     * 压力机名称
     */
    @ApiModelProperty(value = "压力机名称")
    @NotNull
    private String name;

    /**
     * 压力机IP
     */
    @ApiModelProperty(value = "压力机IP")
    @NotNull
    private String ip;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String flag;

    /**
     * cpu核数
     */
    @ApiModelProperty(value = "cpu核数")
    @NotNull
    private Integer cpu;

    /**
     * 内存，单位字节
     */
    @ApiModelProperty(value = "内存大小")
    @NotNull
    private Long memory;

    /**
     * 磁盘，单位字节
     */
    @ApiModelProperty(value = "磁盘大小")
    @NotNull
    private Long disk;

    /**
     * cpu利用率
     */
    @ApiModelProperty(value = "cpu利用率")
    @NotNull
    private BigDecimal cpuUsage;

    /**
     * cpu load
     */
    @ApiModelProperty(value = "cpu load")
    @NotNull
    private BigDecimal cpuLoad;

    /**
     * 内存利用率
     */
    @ApiModelProperty(value = "内存利用率")
    @NotNull
    private BigDecimal memoryUsed;

    /**
     * 磁盘 IO 等待率
     */
    @ApiModelProperty(value = "磁盘IO等待率")
    @NotNull
    private BigDecimal diskIoWait;

    /**
     * 网络带宽入大小
     */
    @ApiModelProperty(value = "网络带宽入大小")
    private Long transmittedIn;

    /**
     * 网络带宽入利用率
     */
    @ApiModelProperty(value = "网络带宽入利用率")
    private BigDecimal transmittedInUsage;

    /**
     * 网络带宽出大小
     */
    @ApiModelProperty(value = "网络带宽出大小")
    private Long transmittedOut;

    /**
     * 网络带宽出利用率
     */
    @ApiModelProperty(value = "网络带宽出利用率")
    private BigDecimal transmittedOutUsage;

    /**
     * 网络带宽利用率
     */
    @ApiModelProperty(value = "网络带宽利用率")
    @NotNull
    private BigDecimal transmittedUsage;

    /**
     * 压测场景id
     */
    @ApiModelProperty(value = "场景id")
    private List<Long> sceneId;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    @ApiModelProperty(value = "机器状态")
    @NotNull
    private Integer status;

    /**
     * 状态 0: 正常 1： 删除
     */
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;


}
