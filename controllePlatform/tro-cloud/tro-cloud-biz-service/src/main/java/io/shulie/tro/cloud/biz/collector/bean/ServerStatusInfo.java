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

package io.shulie.tro.cloud.biz.collector.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务器状态实体类
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.cloud.collector.bean
 * @Date 2020-05-11 14:29
 */
@Getter
@Setter
public class ServerStatusInfo {

    private String ip;
    private float cpu;
    private long memery;
    private LoadInfo loadInfo;
    private String io;
    private List<DiskUsage> diskUsages;

    @Override
    public String toString() {
        return "ServerStatusInfo{" +
            "ip='" + ip + '\'' +
            ", cpu=" + cpu +
            ", memery=" + memery +
            ", loadInfo=" + loadInfo +
            ", io='" + io + '\'' +
            ", diskUsages=" + diskUsages +
            '}';
    }
}
