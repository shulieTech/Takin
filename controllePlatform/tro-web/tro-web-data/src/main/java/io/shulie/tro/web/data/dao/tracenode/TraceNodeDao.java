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

package io.shulie.tro.web.data.dao.tracenode;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.tracenode.TraceNodeInfoParam;
import io.shulie.tro.web.data.result.tracenode.TraceNodeInfoResult;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.tracenode
 * @date 2020/12/29 12:13 下午
 */
public interface TraceNodeDao {
    /**
     * 插入调用栈数据
     * @param params
     */
    void batchInsert(List<TraceNodeInfoParam> params);

    /**
     * 获取节点信息根据 traceId,与rpcId 与 customerId
     * @param traceId
     * @param rpcId
     * @param customerId
     * @param logType
     * @param agentId
     * @param appName
     * @return
     */
    TraceNodeInfoResult getNode(String traceId,String rpcId,Long customerId,Integer logType,String agentId,String appName);

    /**
     * 获取节点信息List
     * @param traceId
     * @param customerId
     * @return
     */
    List<TraceNodeInfoResult> getNodeList(String traceId,Long customerId);

    /**
     * 节点个数
     * @param traceId
     * @param customerId
     * @return
     */
    Integer getNodeCount(String traceId,Long customerId);

    /**
     * 异常节点个数
     * @param traceId
     * @param customerId
     * @return
     */
    Long getExceptionNodeCount(String traceId,Long customerId);

    /**
     * 未知节点个数
     * @param traceId
     * @param customerId
     * @return
     */
    Long getUnknownNodeCount(String traceId,Long customerId);

    /**
     * 未知节点list
     * @param traceId
     * @param customerId
     * @return
     */
    List<TraceNodeInfoResult> getUnknownNodes(String traceId,Long customerId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    TraceNodeInfoResult getById(Long id);



}
