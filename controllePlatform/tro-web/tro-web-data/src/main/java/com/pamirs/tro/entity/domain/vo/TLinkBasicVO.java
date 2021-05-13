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
import java.util.Date;
import java.util.List;

/**
 * 基础链路 压测 封装类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class TLinkBasicVO implements Serializable {

    private static final long serialVersionUID = 1334398427959885883L;

    /**
     * 基础链路id
     */
    private String linkId;

    /**
     * 基础链路名称
     */
    private String linkName;

    /**
     * 阿斯旺场景id
     */
    private String aswanId;

    /**
     * 压测状态(0代表压测完成,1代表正在压测)
     */
    private Integer status;

    /**
     * 压测开始时间
     */
    private Date startTime;

    /**
     * 压测结束时间
     */
    private Date endTime;

    /**
     * 二级业务链路 TPS 计算规则，0:不计算;1:需要计算
     */
    private Integer isTpsCount;

    //压测统计信息分析
    private TScenario statistics;

    //压测统计信息分析（每分钟）
    private List<TScenario> statisticsPerMinute;

    //请求达标率(%)
    private String rtSa;

    //请求标准毫秒值
    private String rt;

    //吞吐量(每秒完成事务数量)
    private String tps;

    //目标成功率(%)
    private String targetSuccessRate;

    /**
     * Gets the value of startTime.
     *
     * @return the value of startTime
     * @author shulie
     * @version 1.0
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the startTime.
     *
     * <p>You can use getStartTime() to get the value of startTime</p>
     *
     * @param startTime startTime
     * @author shulie
     * @version 1.0
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the value of endTime.
     *
     * @return the value of endTime
     * @author shulie
     * @version 1.0
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the endTime.
     *
     * <p>You can use getEndTime() to get the value of endTime</p>
     *
     * @param endTime endTime
     * @author shulie
     * @version 1.0
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
     * @return the aswanId
     * @author shulie
     * @version 1.0
     */
    public String getAswanId() {
        return aswanId;
    }

    /**
     * 2018年5月17日
     *
     * @param aswanId the aswanId to set
     * @author shulie
     * @version 1.0
     */
    public void setAswanId(String aswanId) {
        this.aswanId = aswanId;
    }

    /**
     * 2018年5月17日
     *
     * @return the isTpsCount
     * @author shulie
     * @version 1.0
     */
    public Integer getIsTpsCount() {
        return isTpsCount;
    }

    /**
     * 2018年5月17日
     *
     * @param isTpsCount the isTpsCount to set
     * @author shulie
     * @version 1.0
     */
    public void setIsTpsCount(Integer isTpsCount) {
        this.isTpsCount = isTpsCount;
    }

    /**
     * 2018年5月17日
     *
     * @return the statistics
     * @author shulie
     * @version 1.0
     */
    public TScenario getStatistics() {
        return statistics;
    }

    /**
     * 2018年5月17日
     *
     * @param statistics the statistics to set
     * @author shulie
     * @version 1.0
     */
    public void setStatistics(TScenario statistics) {
        this.statistics = statistics;
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

    public List<TScenario> getStatisticsPerMinute() {
        return statisticsPerMinute;
    }

    public void setStatisticsPerMinute(List<TScenario> statisticsPerMinute) {
        this.statisticsPerMinute = statisticsPerMinute;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRtSa() {
        return rtSa;
    }

    public void setRtSa(String rtSa) {
        this.rtSa = rtSa;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getTps() {
        return tps;
    }

    public void setTps(String tps) {
        this.tps = tps;
    }

    public String getTargetSuccessRate() {
        return targetSuccessRate;
    }

    public void setTargetSuccessRate(String targetSuccessRate) {
        this.targetSuccessRate = targetSuccessRate;
    }
}
