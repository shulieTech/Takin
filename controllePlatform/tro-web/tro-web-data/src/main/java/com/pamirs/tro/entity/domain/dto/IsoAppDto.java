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
import java.util.ArrayList;
import java.util.List;

public class IsoAppDto implements Serializable {

    //ip
    private String ip;

    //是否隔离 Y N
    private String isolationTag;

    //dubbo 注册中心
    private String dubboRegister;

    //euraka注册中心
    private String eurakaRegister;

    //rocket mq name server列表
    private List<IsoAppRocketMqDto> rockMqNameServers = new ArrayList<IsoAppRocketMqDto>();//rockMqNameServers

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsolationTag() {
        return isolationTag;
    }

    public void setIsolationTag(String isolationTag) {
        this.isolationTag = isolationTag;
    }

    public String getDubboRegister() {
        return dubboRegister;
    }

    public void setDubboRegister(String dubboRegister) {
        this.dubboRegister = dubboRegister;
    }

    public String getEurakaRegister() {
        return eurakaRegister;
    }

    public void setEurakaRegister(String eurakaRegister) {
        this.eurakaRegister = eurakaRegister;
    }

    public List<IsoAppRocketMqDto> getRockMqNameServers() {
        return rockMqNameServers;
    }

    public void setRockMqNameServers(List<IsoAppRocketMqDto> rockMqNameServers) {
        this.rockMqNameServers = rockMqNameServers;
    }

}
