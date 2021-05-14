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

package com.pamirs.tro.entity.domain.entity.linkmanage.figure;

import lombok.Data;

/**
 * @Author: guohz
 * @ClassName: LinkEdge
 * @Package: com.pamirs.pradar.graph
 * @Date: 2019/12/5 4:27 下午
 * @Description: 封装返回边数据
 */
@Data
public class LinkEdge {
    private String secondIndex;
    private Object id;
    private Object inVid;
    private Object outVid;
    private String edgeId;
    private String tradeAppName;
    private String rpcId;
    private Integer rpcType;
    private String serviceName;
    private String applicationName;
    private Long invokeCount;
    private Long createTime;
    private Long updateTime;
    private String requestParam;
    private String event;

    public LinkEdge(String secondIndex, Object id, Object inVid, Object outVid, String edgeId, String tradeAppName,
        String applicationName, String rpcId, Integer rpcType, String serviceName, Long invokeCount, Long createTime,
        Long updateTime, String requestParam, String event) {
        this.id = id;
        this.inVid = inVid;
        this.outVid = outVid;
        this.edgeId = edgeId;
        this.tradeAppName = tradeAppName;
        this.applicationName = applicationName;
        this.rpcId = rpcId;
        this.rpcType = rpcType;
        this.serviceName = serviceName;
        this.invokeCount = invokeCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.secondIndex = secondIndex;
        this.requestParam = requestParam;
        this.event = event;
    }
}
