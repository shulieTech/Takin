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

import java.math.BigDecimal;
import java.util.List;

/**
 * 业务拓扑图
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class TBusinessTopology {

    /**
     * 阿旺斯场景ID
     */
    private String scenarioId;

    /**
     * tps计算规则
     */
    private String tpsRule;

    /**
     * 请求达标率
     */
    private String rtSa;

    /**
     * 链路名称
     */
    private String linkId;

    /**
     * 链路等级
     */
    private String linkRank;

    /**
     * 链路名称
     */
    private String linkName;

    /**
     * 下级链路
     */
    private List<TBusinessTopology> lowerLinks;

    /**
     * 失败事务数
     */
    private Integer failedCount;

    /**
     * RT
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
     * TPS
     */
    private Integer tps;

    /**
     * 2018年5月17日
     *
     * @return the scenarioId
     * @author shulie
     * @version 1.0
     */
    public String getScenarioId() {
        return scenarioId;
    }

    /**
     * 2018年5月17日
     *
     * @param scenarioId the scenarioId to set
     * @author shulie
     * @version 1.0
     */
    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * 2018年5月17日
     *
     * @return the tpsRule
     * @author shulie
     * @version 1.0
     */
    public String getTpsRule() {
        return tpsRule;
    }

    /**
     * 2018年5月17日
     *
     * @param tpsRule the tpsRule to set
     * @author shulie
     * @version 1.0
     */
    public void setTpsRule(String tpsRule) {
        this.tpsRule = tpsRule;
    }

    /**
     * 2018年5月17日
     *
     * @return the rtSa
     * @author shulie
     * @version 1.0
     */
    public String getRtSa() {
        return rtSa;
    }

    /**
     * 2018年5月17日
     *
     * @param rtSa the rtSa to set
     * @author shulie
     * @version 1.0
     */
    public void setRtSa(String rtSa) {
        this.rtSa = rtSa;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkId
     * @author shulie
     * @version 1.0
     */
    public String getLinkId() {
        return linkId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkId the linkId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkRank
     * @author shulie
     * @version 1.0
     */
    public String getLinkRank() {
        return linkRank;
    }

    /**
     * 2018年5月17日
     *
     * @param linkRank the linkRank to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkRank(String linkRank) {
        this.linkRank = linkRank;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkName
     * @author shulie
     * @version 1.0
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * 2018年5月17日
     *
     * @param linkName the linkName to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * 2018年5月17日
     *
     * @return the lowerLinks
     * @author shulie
     * @version 1.0
     */
    public List<TBusinessTopology> getLowerLinks() {
        return lowerLinks;
    }

    /**
     * 2018年5月17日
     *
     * @param lowerLinks the lowerLinks to set
     * @author shulie
     * @version 1.0
     */
    public void setLowerLinks(List<TBusinessTopology> lowerLinks) {
        this.lowerLinks = lowerLinks;
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
     * 重写hashCode方法，判断对象是否相等
     * 2018年5月17日
     *
     * @return hashCode值
     * @author shulie
     * @version 1.0
     */
    @Override
    public int hashCode() {
        return this.linkId.hashCode();
    }

    /**
     * 重写equals方法，判断对象是否相等
     * 2018年5月17日
     *
     * @return true相等 false不相等
     * @author shulie
     * @version 1.0
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() == obj.getClass()) {
            return (((TBusinessTopology)obj).getLinkId().equals(this.getLinkId()));
        }
        return false;
    }

}
