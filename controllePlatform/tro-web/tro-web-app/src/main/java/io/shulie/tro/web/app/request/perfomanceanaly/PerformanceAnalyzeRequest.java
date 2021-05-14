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
import lombok.Data;

/**
 * @ClassName PerformanceAnalyRequest
 * @Description 性能分析
 * @Author qianshui
 * @Date 2020/11/4 上午11:55
 */
@Data
@ApiModel("线程分析入参")
public class PerformanceAnalyzeRequest extends PerformanceCommonRequest {
    private static final long serialVersionUID = 729055355150957920L;

    /**
     * 根据报告id，确定起止时间
     */
    private Long reportId;

}
