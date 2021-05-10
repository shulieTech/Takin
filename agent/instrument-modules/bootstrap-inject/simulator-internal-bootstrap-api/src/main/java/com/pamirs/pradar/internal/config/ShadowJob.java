/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.internal.config;

import java.io.Serializable;
import java.util.Map;

/**
 * 影子 Job的配置
 *
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2020-03-18 14:51
 */
public class ShadowJob implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 是否是运行状态 (0:是 1:否)
     */
    private Integer active;
    /**
     * 是否可用状态(0:是 1:否)
     */
    private String status;
    /**
     * job 的 class 名称
     */
    private String className;
    /**
     * job 类型
     */
    private String jobType;
    /**
     * job 数据类型
     */
    private String jobDataType;

    /**
     * 执行的 cron 表达式
     */
    private String cron;
    /**
     *
     */
    private String listenerName;
    /**
     * 错误信息
     */
    private String errorMessage;

    public Map<String, Object> getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(Map<String, Object> extendParam) {
        this.extendParam = extendParam;
    }

    private Map<String, Object> extendParam;

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDataType() {
        return jobDataType;
    }

    public void setJobDataType(String jobDataType) {
        this.jobDataType = jobDataType;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getListenerName() {
        return listenerName;
    }

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShadowJob shaDowJob = (ShadowJob) o;

        return className.equals(shaDowJob.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ShadowJob{" +
                "id=" + id +
                ", active=" + active +
//                ", status='" + status + '\'' +
                ", className='" + className + '\'' +
                ", jobType='" + jobType + '\'' +
                ", jobDataType='" + jobDataType + '\'' +
                ", cron='" + cron + '\'' +
                ", listenerName='" + listenerName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", extendParam=" + extendParam +
                '}';
    }
}
