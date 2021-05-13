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

package com.pamirs.tro.entity.domain.vo.report;

import lombok.Data;

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.entity.domain.vo.report
 * @date 2020/9/30 5:59 下午
 */
@Data
public class ReportFeature {
    private Long reportId;
    private Integer state;
    private String key;
    private String value;

    public ReportFeature(Long reportId, Integer state, String key, String value) {
        this.state = state;
        this.reportId = reportId;
        this.key = key;
        this.value = value;
    }
}
