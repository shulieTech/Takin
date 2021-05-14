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

import java.util.List;

import io.shulie.tro.web.common.vo.perfomanceanaly.MemoryEntryVO;
import lombok.Data;

/**
 * @ClassName PerformanceBaseDataParam
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:35
 */
@Data
public class PerformanceBaseDataParam {

    private String agentId;

    private String appName;

    private String appIp;

    private Long processId;

    /**
     * old memory used
     */
    private Long oldMemory;

    /**
     * total old memory
     */
    private Long totalOld;
    /**
     * perm memory used
     */
    private Long permMemory;

    /**
     * total perm memory
     */
    private Long totalPerm;

    /**
     * young memory used
     */
    private Long youngMemory;

    /**
     * total young memory
     */
    private Long totalYoung;

    /**
     * 非堆内存大小
     */
    private Long totalNonHeapMemory;

    /**
     * buffer pool 总内存大小
     */
    private Long totalBufferPoolMemory;

    private Long totalMemory;

    private Integer youngGcCount;

    /**
     * young gc 次数
     */
    private Integer fullGcCount;

    /**
     * young gc 耗时
     */
    private Long youngGcCost;

    /**
     * full gc 耗时
     */
    private Long fullGcCost;

    private String processName;

    private Long timestamp;

    private List<PerformanceThreadDataParam> threadDataList;

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
