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

package io.shulie.tro.cloud.data.param.report;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.param.report
 * @date 2021/2/1 7:18 下午
 */
@Data
public class ReportUpdateConclusionParam {
    private Long id;

    /**
     * 压测结论: 0/不通过，1/通过
     */
    private Integer conclusion;


    /**
     * 扩展字段，JSON数据格式
     */
    private String features;
}
