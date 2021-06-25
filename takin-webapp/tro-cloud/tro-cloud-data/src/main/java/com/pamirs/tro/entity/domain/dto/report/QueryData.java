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

package com.pamirs.tro.entity.domain.dto.report;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-04-21
 */
@Data
public class QueryData {

    /**
     *
     */
    private String time;

    /**
     * RT
     */
    private BigDecimal avgRt;

    /**
     * SA
     */
    private BigDecimal sa;

    /**
     * 成功率
     */
    private BigDecimal successRate;

    /**
     * TPS
     */
    private BigDecimal tps;
}
