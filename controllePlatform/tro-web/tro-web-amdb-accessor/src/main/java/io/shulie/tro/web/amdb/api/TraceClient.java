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

package io.shulie.tro.web.amdb.api;

import java.util.List;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.RpcStack;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.bean.query.trace.TraceInfoQueryDTO;
import io.shulie.tro.web.amdb.bean.result.trace.EntryTraceInfoDTO;

/**
 * @author shiyajian
 * create: 2020-10-12
 */
public interface TraceClient {

    /**
     * 查询入口的trace请求流量信息
     * 用于压测实况，还有压测报告中的请求流量明细功能
     */
    PagingList<EntryTraceInfoDTO> listEntryTraceInfo(TraceInfoQueryDTO query);

    /**
     * 根据 traceId 查询Trace的调用栈
     */
    RpcStack getTraceDetailById(String traceId);

    /**
     * 根据traceID 查询base
     * @param traceId
     * @return
     */
    List<RpcBased> getTraceBaseById(String traceId);

}
