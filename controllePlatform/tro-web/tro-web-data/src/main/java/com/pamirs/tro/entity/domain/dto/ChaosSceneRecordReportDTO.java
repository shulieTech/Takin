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

package com.pamirs.tro.entity.domain.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: 710524
 * @ClassName: ChaosSceneRecordReportDTO
 * @package: com.pamirs.tro.entity.domain.vo.chaos
 * @Date: 2019年 05月16日 09:26
 * @Description: 故障检测报告详情
 */
public class ChaosSceneRecordReportDTO implements Serializable {

    private Long sceneId;//场景编号
    private String sceneName;//场景名称
    private String sceneDescription;//场景描述
    private Long appId;//所属应用编号
    private String appName;//所属应用名称
    private Integer hostCount = 0;//主机数

    private String hostString;//主机IP

    private Long recordId;//记录ID
    private Integer chaosSuccessCount = 0;//注入成功节点数
    private Integer chaosErrCount = 0;//注入失败的节点数
    private Integer noExpectCount = 0;//不符合预期节点数
    private Integer errCount = 0;//异常节点数

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date startTime;//开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date endTime;//结束时间

    private Long executeTime = 0L;//演练时长

    private Long cmdCount = 0L;//命令总数
    private Integer executeCount = 0;//已经执行的个数

    private Integer status;

    private Integer percentage;
    private Integer step;

    private List<ChaosFailureReportDTO> failureReports;

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneDescription() {
        return sceneDescription;
    }

    public void setSceneDescription(String sceneDescription) {
        this.sceneDescription = sceneDescription;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getHostCount() {
        return hostCount;
    }

    public void setHostCount(Integer hostCount) {
        this.hostCount = hostCount;
    }

    public Integer getChaosSuccessCount() {
        return chaosSuccessCount;
    }

    public void setChaosSuccessCount(Integer chaosSuccessCount) {
        this.chaosSuccessCount = chaosSuccessCount;
    }

    public Integer getChaosErrCount() {
        return chaosErrCount;
    }

    public void setChaosErrCount(Integer chaosErrCount) {
        this.chaosErrCount = chaosErrCount;
    }

    public Integer getNoExpectCount() {
        return noExpectCount;
    }

    public void setNoExpectCount(Integer noExpectCount) {
        this.noExpectCount = noExpectCount;
    }

    public Integer getErrCount() {
        return errCount;
    }

    public void setErrCount(Integer errCount) {
        this.errCount = errCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<ChaosFailureReportDTO> getFailureReports() {
        return failureReports;
    }

    public void setFailureReports(List<ChaosFailureReportDTO> failureReports) {
        this.failureReports = failureReports;
    }

    public String getHostString() {
        return hostString;
    }

    public void setHostString(String hostString) {
        this.hostString = hostString;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getCmdCount() {
        if (cmdCount == null) {
            return 0L;
        }
        return cmdCount;
    }

    public void setCmdCount(Long cmdCount) {
        this.cmdCount = cmdCount;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public Integer getStatus() {
        return status;
    }

    public ChaosSceneRecordReportDTO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public ChaosSceneRecordReportDTO setPercentage(Integer percentage) {
        this.percentage = percentage;
        return this;
    }

    public Integer getStep() {
        return step;
    }

    public ChaosSceneRecordReportDTO setStep(Integer step) {
        this.step = step;
        return this;
    }

    public Long getRecordId() {
        return recordId;
    }

    public ChaosSceneRecordReportDTO setRecordId(Long recordId) {
        this.recordId = recordId;
        return this;
    }
}
