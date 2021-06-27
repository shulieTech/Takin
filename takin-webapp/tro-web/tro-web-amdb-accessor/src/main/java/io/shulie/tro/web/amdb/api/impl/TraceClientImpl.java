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

package io.shulie.tro.web.amdb.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.google.common.collect.Lists;
import com.pamirs.pradar.log.parser.ProtocolParserFactory;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import com.pamirs.pradar.log.parser.trace.RpcStack;
import com.pamirs.tro.common.enums.amdb.common.request.trace.EntryTraceQueryParam;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.TraceClient;
import io.shulie.tro.web.amdb.bean.common.AmdbResult;
import io.shulie.tro.web.amdb.bean.query.trace.TraceInfoQueryDTO;
import io.shulie.tro.web.amdb.bean.result.trace.EntryTraceInfoDTO;
import io.shulie.tro.web.amdb.util.HttpClientUtil;
import io.shulie.tro.web.common.util.ActivityUtil;
import io.shulie.tro.web.common.util.ActivityUtil.EntranceJoinEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.AmdbClientProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author shiyajian
 * create: 2020-10-12
 */
@Component
@Slf4j
public class TraceClientImpl implements TraceClient {

    //private static final String QUERY_TRACE_PATH = "/api/QueryTrace?query=@TraceId@";
    private static final String QUERY_TRACE_PATH = "/amdb/trace/getTraceDetail?traceId=@TraceId@";
    private static final String ENTRY_TRACE_PATH = "/amdb/trace/getEntryTraceList";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AmdbClientProperties properties;

    @Override
    public PagingList<EntryTraceInfoDTO> listEntryTraceInfo(TraceInfoQueryDTO query) {
        String url = properties.getUrl().getAmdb() + ENTRY_TRACE_PATH;
        try {
            EntryTraceQueryParam queryParam = new EntryTraceQueryParam();
            queryParam.setStartTime(query.getStartTime());
            queryParam.setEndTime(query.getEndTime());
            queryParam.setResultType(query.getType());
            List<String> entranceList = query.getEntranceList().stream().map(entrance -> {
                EntranceJoinEntity entranceJoinEntity = ActivityUtil.covertEntrance(entrance);
                return entranceJoinEntity.getApplicationName() + "#" + entranceJoinEntity.getServiceName() + "#"
                    + entranceJoinEntity.getMethodName()+"#"+entranceJoinEntity.getRpcType();
            }).collect(Collectors.toList());
            queryParam.setEntranceList(StringUtils.join(entranceList, ","));
            queryParam.setCurrentPage(query.getPageNum());
            queryParam.setPageSize(query.getPageSize());
            queryParam.setFieldNames("appName,serviceName,methodName,remoteIp,port,resultCode,cost,startTime,traceId");
            String responseEntity = HttpClientUtil.sendGet(url, queryParam);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往pardar查询应用的接口信息报错,请求地址：{}，响应信息：{}", url, responseEntity);
                return PagingList.empty();
            } else {
                AmdbResult<List<com.pamirs.tro.common.enums.amdb.common.trace.EntryTraceInfoDTO>> amdbResponse = JSON.parseObject(
                    responseEntity,
                    new TypeReference<AmdbResult<List<com.pamirs.tro.common.enums.amdb.common.trace.EntryTraceInfoDTO>>>() {
                    });
                if (CollectionUtils.isNotEmpty(amdbResponse.getData())) {
                    List<EntryTraceInfoDTO> result = amdbResponse.getData().stream().map(entry -> {
                        EntryTraceInfoDTO dto = new EntryTraceInfoDTO();
                        dto.setAppName(entry.getAppName());
                        dto.setEntry(entry.getServiceName());
                        dto.setMethod(entry.getMethodName());
                        dto.setProcessTime(entry.getCost());
                        dto.setStartTime(entry.getStartTime());
                        //TODO
                        dto.setId("0");
                        //TODO
                        dto.setEndTime(entry.getStartTime());
                        dto.setStatus(entry.getResultCode());
                        dto.setTraceId(entry.getTraceId());
                        return dto;
                    }).collect(Collectors.toList());
                    return PagingList.of(result, amdbResponse.getTotal());
                }
            }
        } catch (Exception e) {
            log.error("前往pardar查询入口的请求流量明细：{}", JSON.toJSONString(query), e);
        }
        return PagingList.empty();
    }

    @Override
    public RpcStack getTraceDetailById(String traceId) {
        try {
            String url = properties.getUrl().getAmdb() + QUERY_TRACE_PATH.replace("@TraceId@", traceId);
            String responseEntity = HttpClientUtil.sendGet(url);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往pardar查询应用的接口信息报错,请求地址：{}，响应信息：{}", url, responseEntity);
            } else {
                AmdbResult<List<RpcBased>> amdbResponse = JSON.parseObject(responseEntity,
                    new TypeReference<AmdbResult<List<RpcBased>>>() {
                    });
                return ProtocolParserFactory.getFactory().parseRpcStackByRpcBase(traceId, amdbResponse.getData());
            }
        } catch (Exception e) {
            log.error("前往pardar查询Trace调用栈异常，traceId:{}",
                traceId, e);
        }
        return ProtocolParserFactory.getFactory().parseRpcStackByRpcBase(traceId, new ArrayList<>());
    }
    @Override
    public List<RpcBased> getTraceBaseById(String traceId) {
        try {
            String url = properties.getUrl().getAmdb() + QUERY_TRACE_PATH.replace("@TraceId@", traceId);
            String responseEntity = HttpClientUtil.sendGet(url);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往pardar查询应用的接口信息报错,请求地址：{}，响应信息：{}", url, responseEntity);
            } else {
                AmdbResult<List<RpcBased>> amdbResponse = JSON.parseObject(responseEntity,
                    new TypeReference<AmdbResult<List<RpcBased>>>() {
                    });
                return amdbResponse.getData() ;
            }
        } catch (Exception e) {
            log.error("前往pardar查询Trace调用栈异常，traceId:{}",
                traceId, e);
        }
        return Lists.newArrayList();
    }
}
