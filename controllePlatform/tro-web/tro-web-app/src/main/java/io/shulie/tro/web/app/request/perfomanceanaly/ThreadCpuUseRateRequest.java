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

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ThreadCpuUseRateRequest
 * @Description 线程cpu利用率
 * @Author qianshui
 * @Date 2020/11/4 上午11:30
 */
@Data
@ApiModel("线程cpu占用率入参")
public class ThreadCpuUseRateRequest extends PerformanceCommonRequest implements Serializable {
    private static final long serialVersionUID = -7182637518514949578L;

    @ApiModelProperty(value = "报告id")
    private Long reportId;


    @ApiModelProperty(value = "线程名称")
    private String threadName;

}
