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

package io.shulie.amdb.response.instance;

import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class AmdbAppInstanceResponse implements Serializable {
    // 应用 id
    Long appId;
    // 应用名称
    String appName;
    // 用来判断实例的唯一性
    String agentId;
    // agent 版本
    String agentVersion;
    // 包的md5值
    String agentMd5;
    // 更新时间
    String agentUpdateTime;
    // 进程号 id
    String progressId;
    // ip 地址
    String ipAddress;
    // agent 支持的语言
    String agentLanguage;
    // ext
    String ext;
    // flag
    int flag;
    // hostname
    String hostname;
    // pid
    String pid;

    public AmdbAppInstanceResponse(){

    }

    public AmdbAppInstanceResponse(TAmdbAppInstanceDO amdbAppInstance) {
        this.setAppId(amdbAppInstance.getAppId());
        this.setAppName(amdbAppInstance.getAppName());
        this.setAgentId(amdbAppInstance.getAgentId());
        this.setAgentVersion(amdbAppInstance.getAgentVersion());
        this.setAgentMd5(amdbAppInstance.getMd5());
        this.setAgentUpdateTime(DateFormatUtils.format(amdbAppInstance.getGmtModify(),"yyyy-MM-dd HH:mm:ss"));
        this.setProgressId(amdbAppInstance.getPid());
        this.setIpAddress(amdbAppInstance.getIp());
        this.setAgentLanguage(amdbAppInstance.getAgentLanguage());
        this.setExt(amdbAppInstance.getExt());
        this.setFlag(amdbAppInstance.getFlag());
        this.setHostname(amdbAppInstance.getHostname());
        this.pid=amdbAppInstance.getPid();
    }
}