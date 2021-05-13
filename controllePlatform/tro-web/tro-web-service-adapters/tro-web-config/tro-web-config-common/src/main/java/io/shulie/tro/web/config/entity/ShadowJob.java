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

package io.shulie.tro.web.config.entity;

import java.io.Serializable;

import io.shulie.tro.web.config.enums.ShadowJobType;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public class ShadowJob implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * Job 类型
     */
    private ShadowJobType type;

    private String className;

    private String cron;

    private String jobDataType;

    private String listener;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShadowJobType getType() {
        return type;
    }

    public void setType(ShadowJobType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getJobDataType() {
        return jobDataType;
    }

    public void setJobDataType(String jobDataType) {
        this.jobDataType = jobDataType;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
