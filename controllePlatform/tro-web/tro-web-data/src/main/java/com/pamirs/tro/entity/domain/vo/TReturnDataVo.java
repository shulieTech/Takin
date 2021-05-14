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

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 298403
 * 上传 抽取增量数据 jar包 实体类
 */
public class TReturnDataVo {

    /**
     * 抽取数主键
     */
    private String trdId;
    /**
     * 负责人工号
     */
    private String principalNo;

    /**
     * 链路id
     */
    @NotBlank
    private String linkId;

    /**
     * 链路名称
     */
    private String linkName;

    /**
     * 启动类名
     */
    @NotBlank
    private String startClass;

    /**
     * jar包文件
     */
    @JsonIgnore
    private MultipartFile jarFile;

    /**
     * jar包路劲
     */
    private String jarPah;

    /**
     * jar包名称
     */
    private String jarName;

    /**
     * jar包执行状态
     */
    private Integer loadStatus;

    /**
     * 阿斯旺id
     */
    private String aswanId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date updateTime;

    public String getTrdId() {
        return trdId;
    }

    public void setTrdId(String trdId) {
        this.trdId = trdId;
    }

    public String getPrincipalNo() {
        return principalNo;
    }

    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getStartClass() {
        return startClass;
    }

    public void setStartClass(String startClass) {
        this.startClass = startClass;
    }

    public MultipartFile getJarFile() {
        return jarFile;
    }

    public void setJarFile(MultipartFile jarFile) {
        this.jarFile = jarFile;
    }

    public String getJarPah() {
        return jarPah;
    }

    public void setJarPah(String jarPah) {
        this.jarPah = jarPah;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public Integer getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(Integer loadStatus) {
        this.loadStatus = loadStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAswanId() {
        return aswanId;
    }

    public void setAswanId(String aswanId) {
        this.aswanId = aswanId;
    }

    @Override
    public String toString() {
        return "TReturnDataVo{" +
            "trdId='" + trdId + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", linkId='" + linkId + '\'' +
            ", linkName='" + linkName + '\'' +
            ", startClass='" + startClass + '\'' +
            ", jarFile=" + jarFile +
            ", jarPah='" + jarPah + '\'' +
            ", jarName='" + jarName + '\'' +
            ", loadStatus=" + loadStatus +
            ", aswanId='" + aswanId + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
