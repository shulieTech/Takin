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
import java.math.RoundingMode;

import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-04-21
 */
@Data
public class StatReportDTO {

    /**
     * 时间
     */
    private String time;

    /**
     * 总请求
     */
    private Long totalRequest;

    /**
     * 失败的总次数
     */
    private Long failRequest;

    /**
     * tps
     */
    private BigDecimal tps;

    /**
     * Rt和
     */
    private BigDecimal sumRt;

    /**
     * Sa总计数
     */
    private BigDecimal saCount;

    /**
     * minRt
     */
    private BigDecimal minRt;

    /**
     * maxRt
     */
    private BigDecimal maxRt;

    /**
     * maxTps
     */
    private BigDecimal maxTps;

    /**
     * 获取SA
     * sa总数/总请求*100
     *
     * @return
     */
    public BigDecimal getSa() {
        return saCount.divide(new BigDecimal(getTotalRequest()), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
    }

    /**
     * 成功率
     * (总次数-失败次数)/总次数*100
     *
     * @return
     */
    public BigDecimal getSuccessRate() {
        return new BigDecimal(getTotalRequest() - getFailRequest()).divide(new BigDecimal(getTotalRequest()), 4,
            RoundingMode.HALF_UP).multiply(new BigDecimal(100));
    }

    /**
     * 平均RT时间
     * sum(sum_rt)/totalRequest
     *
     * @return
     */
    public BigDecimal getAvgRt() {
        return sumRt.divide(new BigDecimal(getTotalRequest()), 2, RoundingMode.HALF_UP);
    }

}
