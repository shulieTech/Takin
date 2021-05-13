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

package io.shulie.tro.web.amdb.bean.result.application;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ApplicationNodeDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    // 应用 id
    private Long appId;
    // 应用名称
    private String appName;
    // 探针id
    private String agentId;
    //探针开发语言
    private String agentLanguage;
    // 探针版本
    private String agentVersion;
    //探针md5值
    private String agentMd5;
    // 探针最后修改时间
    private Date agentUpdateTime;
    //进程号
    private String progressId;
    //ip地址
    private String ipAddress;
}
