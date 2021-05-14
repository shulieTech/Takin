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

package io.shulie.tro.web.data.result.application;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/11 7:43 下午
 * @Description:
 */
@Data
public class ApplicationDetailResult {
    private Long applicationId;
    private String applicationName;
    private String applicationDesc;
    private String ddlScriptPath;
    private String cleanScriptPath;
    private String readyScriptPath;
    private String basicScriptPath;
    private String cacheScriptPath;
    private Long cacheExpTime;
    private Integer useYn;
    private String agentVersion;
    private Integer nodeNum;
    private Integer accessStatus;
    private String switchStatus;
    private String exceptionInfo;
    private Date createTime;
    private Date updateTime;
    private String alarmPerson;
    private String pradarVersion;
    private Long customerId;
    private Long userId;
}
