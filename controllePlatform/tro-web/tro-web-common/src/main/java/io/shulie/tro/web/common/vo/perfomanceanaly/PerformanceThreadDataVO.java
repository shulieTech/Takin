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

package io.shulie.tro.web.common.vo.perfomanceanaly;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PerformanceThreadDataVO
 * @Description
 * @Author qianshui
 * @Date 2020/11/10 上午10:48
 */
@Data
@ApiModel("线程数据")
public class PerformanceThreadDataVO implements Serializable {
    private static final long serialVersionUID = 3355440323176991407L;
    /**
     * 线程 id
     */
    @ApiModelProperty(value = "id")
    private String threadId;
    /**
     * 线程名称
     */
    @ApiModelProperty(value = "线程名称")
    private String threadName;

    /**
     * 线程组名称
     */
    private String groupName;
    /**
     * cpu 占用率
     */
    @ApiModelProperty(value = "线程cpu占用率")
    private Double threadCpuUsage;

    /**
     * cpu 占用时长
     */
    private Long cpuTime;

    /**
     * 是否中断
     */
    private Boolean interrupted;
    /**
     * 线程状态
     */
    @ApiModelProperty(value = "线程状态")
    private String threadStatus;

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 锁拥有者名称
     */
    private String lockOwnerName;

    /**
     * 锁拥有者 ID
     */
    private Long lockOwnerId;

    /**
     * 是否是暂停状态
     */
    private Boolean suspended;

    /**
     * 线程是否在 native代码执行中
     */
    private Boolean inNative;

    /**
     * 是否是后台运行
     */
    private Boolean daemon;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 阻塞时间
     */
    private Long blockedTime;

    /**
     * 阻塞次数
     */
    private Long blockedCount;

    /**
     * 等待时间
     */
    private Long waitedTime;

    /**
     * 等待次数
     */
    private Long waitedCount;

    /**
     * 线程堆栈
     */
    @ApiModelProperty(value = "线程堆栈")
    private String threadStack;

    /**
     * lock identity hash code
     */
    private Integer lockIdentityHashCode;

    /**
     * 阻塞的线程数
     */
    private Integer blockingThreadCount;

    /**
     * 链路追踪的 traceId
     */
    private String traceId;

    /**
     * 链路追踪的 rpcId
     */
    private String rpcId;

    /**
     * 是否是压测流量
     */
    private Boolean isClusterTest;


}
