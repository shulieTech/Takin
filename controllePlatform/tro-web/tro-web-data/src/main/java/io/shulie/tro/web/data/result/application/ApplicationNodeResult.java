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

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-09-23 19:01
 * @Description: 节点信息
 */

@Data
public class ApplicationNodeResult {

    private String appId;
    private String appName;
    private String agentId;
    private String nodeIp;
    private String processNo;
    private String agentLanguage;
    private String agentVersion;
    private String md5Value;
    private String createTime;
    private String updateTime;

}
