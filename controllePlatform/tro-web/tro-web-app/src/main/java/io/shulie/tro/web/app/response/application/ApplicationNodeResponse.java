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

package io.shulie.tro.web.app.response.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-09-23 19:01
 * @Description: 节点信息
 */

@Data
public class ApplicationNodeResponse {

    @JsonProperty("agentId")
    private String agentId;

    @JsonProperty("ip")
    private String nodeIp;

    @JsonProperty("processNumber")
    private String processNo;

    @JsonProperty("agentLang")
    private String agentLanguage;

    @JsonProperty("agentVersion")
    private String agentVersion;

    @JsonProperty("updateTime")
    private String updateTime;

}
