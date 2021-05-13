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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 应用配置开关
 *
 * @author 298403
 * @date 2019-03-28
 */
public class TApplicationMntConfigVo {

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 应用id
     */

    private String applicationId;

    /**
     * 应用id 列表 , 分割
     */
    @NotBlank(message = "应用id列表不能为空")
    private String applicationIds;

    /**
     * 应用配置id
     */
    private String tamcId;

    /**
     * 防作弊 欺骗检测开关
     */
    @NotBlank(message = "防作弊值不能为空")
    @Min(value = 0)
    @Max(value = 1)
    private String cheatCheck;

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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTamcId() {
        return tamcId;
    }

    public void setTamcId(String tamcId) {
        this.tamcId = tamcId;
    }

    public String getCheatCheck() {
        return cheatCheck;
    }

    public void setCheatCheck(String cheatCheck) {
        this.cheatCheck = cheatCheck;
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

    public String getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(String applicationIds) {
        this.applicationIds = applicationIds;
    }

    @Override
    public String toString() {
        return "TApplicationMntConfigVo{" +
            "applicationName='" + applicationName + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", applicationIds='" + applicationIds + '\'' +
            ", tamcId='" + tamcId + '\'' +
            ", cheatCheck='" + cheatCheck + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }

}
