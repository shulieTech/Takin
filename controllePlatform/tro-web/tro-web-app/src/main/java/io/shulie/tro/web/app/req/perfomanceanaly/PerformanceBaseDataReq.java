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

package io.shulie.tro.web.app.req.perfomanceanaly;

import java.io.Serializable;
import java.util.List;

import io.shulie.tro.web.common.vo.perfomanceanaly.MemoryEntryVO;
import io.shulie.tro.web.common.vo.perfomanceanaly.PerformanceThreadDataVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PerformanceBaseTargetReq
 * @Description 性能基础指标：应用、线程、内存等信息
 * @Author qianshui
 * @Date 2020/11/4 上午10:51
 */
@Data
@ApiModel("性能数据")
public class PerformanceBaseDataReq implements Serializable {

    private static final long serialVersionUID = 4181247024798793931L;

    @ApiModelProperty(value = "agentId")
    private String agentId;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "应用ip")
    private String appIp;

    @ApiModelProperty(value = "进程id")
    private Long processId;

    /**
     * old memory used
     */
    @ApiModelProperty(value = "老年代内存")
    private Long oldMemory;

    /**
     * total old memory
     */
    private Long totalOld;
    /**
     * perm memory used
     */
    @ApiModelProperty(value = "永久代内存")
    private Long permMemory;

    /**
     * total perm memory
     */
    private Long totalPerm;

    /**
     * young memory used
     */
    @ApiModelProperty(value = "新生代内存")
    private Long youngMemory;

    /**
     * total young memory
     */
    private Long totalYoung;

    /**
     * 非堆内存大小
     */
    @ApiModelProperty(value = "非堆内存大小")
    private Long totalNonHeapMemory;

    /**
     * buffer pool 总内存大小
     */
    @ApiModelProperty(value = "buffer pool内存大小")
    private Long totalBufferPoolMemory;

    @ApiModelProperty(value = "总内存")
    private Long totalMemory;

    @ApiModelProperty(value = "young gc次数")
    private Integer youngGcCount;

    /**
     * young gc 次数
     */
    @ApiModelProperty(value = "full gc次数")
    private Integer fullGcCount;

    /**
     * young gc 耗时
     */
    @ApiModelProperty(value = "young gc耗时")
    private Long youngGcCost;

    /**
     * full gc 耗时
     */
    @ApiModelProperty(value = "full gc耗时")
    private Long fullGcCost;

    @ApiModelProperty(value = "进程名称")
    private String processName;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "线程信息")
    private List<PerformanceThreadDataVO> threadDataList;

    /**
     * 堆内存
     */
    private MemoryEntryVO heapMemory;

    /**
     * 堆内存详情
     */
    private List<MemoryEntryVO> heapMemories;

    /**
     * 非堆内存
     */
    private MemoryEntryVO nonheapMemory;

    /**
     * 非堆内存
     */
    private List<MemoryEntryVO> nonheapMemories;

    /**
     * buffer pool 内存
     */
    private List<MemoryEntryVO> bufferPoolMemories;

}
