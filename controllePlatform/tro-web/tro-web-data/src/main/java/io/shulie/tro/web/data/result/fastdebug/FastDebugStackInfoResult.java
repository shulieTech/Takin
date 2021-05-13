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

package io.shulie.tro.web.data.result.fastdebug;

import java.util.Date;
import java.util.Objects;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-31 10:42
 * @Description:
 */

@Data
public class FastDebugStackInfoResult {

    private Long id;
    /**
     * appName
     */
    private String appName;

    /**
     * agentId
     */
    private String agentId;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpcid
     */
    private String rpcId;

    /**
     * 日志级别
     */
    private String level;

    /**
     * type; 0: 客户端； 1：服务端
     */
    private Integer type;

    /**
     * stack信息
     */
    private String content;

    /**
     * 是否stack信息
     */
    private Boolean isStack;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;

    @Override
    public String toString() {
        return  appName + '|' + agentId + '|' +traceId + '|' +  rpcId + '|' + level + '|' + type ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        FastDebugStackInfoResult result = (FastDebugStackInfoResult)o;
        return  Objects.equals(appName, result.appName)
            && Objects.equals(agentId, result.agentId) && Objects.equals(traceId, result.traceId)
            && Objects.equals(rpcId, result.rpcId) && Objects.equals(level, result.level)
            && Objects.equals(type, result.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash( appName, agentId, traceId, rpcId, level, type);
    }
}
