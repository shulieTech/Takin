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

package com.pamirs.tro.entity.domain.entity.report;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ReportBusinessActivityDetail {
    private Long id;

    /**
     * 报告ID
     */
    private Long reportId;

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 业务活动ID
     */
    private Long businessActivityId;

    /**
     * 业务活动名称
     */
    private String businessActivityName;

    /**
     * 请求数
     */
    private Long request;

    /**
     * tps
     */
    private BigDecimal tps;

    /**
     * rt
     */
    private BigDecimal rt;

    /**
     * 成功率
     */
    private BigDecimal successRate;

    /**
     * sa
     */
    private BigDecimal sa;

    /**
     * 最大tps
     */
    private BigDecimal maxTps;

    /**
     * 最大rt
     */
    private BigDecimal maxRt;

    /**
     * 最小rt
     */
    private BigDecimal minRt;

    private String features;

    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtUpdate;
}
