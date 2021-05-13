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

package io.shulie.tro.web.app.response.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-12 14:08
 * @Description:
 */

@Data
@ApiModel(value = "内存图表返回数据")
public class MemoryAnalysisResponse implements Serializable {

    private static final long serialVersionUID = -5427941161243522078L;

    @ApiModelProperty(name = "heapMemory",value = "堆栈信息")
    private List<MemoryModelVo> heapMemory ;

    @ApiModelProperty(name = "gcCount",value = "gc次数")
    private List<MemoryModelVo> gcCount;

    @ApiModelProperty(name = "gcCost",value = "gc耗时")
    private List<MemoryModelVo> gcCost;
}
