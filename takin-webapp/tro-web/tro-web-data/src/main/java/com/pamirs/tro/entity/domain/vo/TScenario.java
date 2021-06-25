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

package com.pamirs.tro.entity.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;

/**
 * 说明: 阿斯旺压测统计信息实体
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 * @since 2018/05/08
 */
public class TScenario implements Serializable {

    private static final long serialVersionUID = -8044824380871303262L;
    /**
     * rt标准，在spt.cainiao.com中配置
     */
    private String rtStandard;

    /**
     * 场景编码
     */
    private String scenarioCode;

    /**
     * 场景id
     */
    private Long scenarioId;

    /**
     * 场景名称
     */
    private String scenarioName;

    /**
     * 失败事务数
     */
    private Integer failedCount;

    /**
     * RT 请求标准毫秒值
     */
    private BigDecimal rt;

    /**
     * rt达标率
     */
    private BigDecimal rtRate;

    /**
     * 成功事务数
     */
    private Integer successCount;

    /**
     * 事务成功率
     */
    private BigDecimal successRate;

    /**
     * TPS 吞吐量(每秒完成事务数量)
     */
    private Integer tps;

    /**
     * yyyy-MM-dd HH:mm
     */
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date timestamp;

    /**
     * 2018年5月17日
     *
     * @return the scenarioCode
     * @author shulie
     * @version 1.0
     */
    public String getScenarioCode() {
        return scenarioCode;
    }

    /**
     * 2018年5月17日
     *
     * @param scenarioCode the scenarioCode to set
     * @author shulie
     * @version 1.0
     */
    public void setScenarioCode(String scenarioCode) {
        this.scenarioCode = scenarioCode;
    }

    /**
     * 2018年5月17日
     *
     * @return the scenarioId
     * @author shulie
     * @version 1.0
     */
    public Long getScenarioId() {
        return scenarioId;
    }

    /**
     * 2018年5月17日
     *
     * @param scenarioId the scenarioId to set
     * @author shulie
     * @version 1.0
     */
    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * 2018年5月17日
     *
     * @return the scenarioName
     * @author shulie
     * @version 1.0
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * 2018年5月17日
     *
     * @param scenarioName the scenarioName to set
     * @author shulie
     * @version 1.0
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * 2018年5月17日
     *
     * @return the failedCount
     * @author shulie
     * @version 1.0
     */
    public Integer getFailedCount() {
        return failedCount;
    }

    /**
     * 2018年5月17日
     *
     * @param failedCount the failedCount to set
     * @author shulie
     * @version 1.0
     */
    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    /**
     * 2018年5月17日
     *
     * @return the rt
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getRt() {
        return rt;
    }

    /**
     * 2018年5月17日
     *
     * @param rt the rt to set
     * @author shulie
     * @version 1.0
     */
    public void setRt(BigDecimal rt) {
        this.rt = rt;
    }

    /**
     * 2018年5月17日
     *
     * @return the rtRate
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getRtRate() {
        return rtRate;
    }

    /**
     * 2018年5月17日
     *
     * @param rtRate the rtRate to set
     * @author shulie
     * @version 1.0
     */
    public void setRtRate(BigDecimal rtRate) {
        this.rtRate = rtRate;
    }

    /**
     * 2018年5月17日
     *
     * @return the successCount
     * @author shulie
     * @version 1.0
     */
    public Integer getSuccessCount() {
        return successCount;
    }

    /**
     * 2018年5月17日
     *
     * @param successCount the successCount to set
     * @author shulie
     * @version 1.0
     */
    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    /**
     * 2018年5月17日
     *
     * @return the successRate
     * @author shulie
     * @version 1.0
     */
    public BigDecimal getSuccessRate() {
        return successRate;
    }

    /**
     * 2018年5月17日
     *
     * @param successRate the successRate to set
     * @author shulie
     * @version 1.0
     */
    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }

    /**
     * 2018年5月17日
     *
     * @return the tps
     * @author shulie
     * @version 1.0
     */
    public Integer getTps() {
        return tps;
    }

    /**
     * 2018年5月17日
     *
     * @param tps the tps to set
     * @author shulie
     * @version 1.0
     */
    public void setTps(Integer tps) {
        this.tps = tps;
    }

    /**
     * 2018年5月17日
     *
     * @return the timestamp
     * @author shulie
     * @version 1.0
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * 2018年5月17日
     *
     * @param timestamp the timestamp to set
     * @author shulie
     * @version 1.0
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取rt标准
     *
     * @return
     */
    public String getRtStandard() {
        return rtStandard;
    }

    /**
     * 设置rt标准
     *
     * @param rtStandard rt标准
     */
    public void setRtStandard(String rtStandard) {
        this.rtStandard = rtStandard;
    }
}
