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

import lombok.Data;

/**
 * @ClassName ThreadCpuUseRateChartResponse
 * @Description 线程cpu利用率图表
 * @Author qianshui
 * @Date 2020/11/4 上午11:36
 */
@Data
public class ThreadCpuUseRateChartResponse extends TimeChartResponse {
    private static final long serialVersionUID = -6280051083663598217L;

    private Double threadCpuUseRate;
}
