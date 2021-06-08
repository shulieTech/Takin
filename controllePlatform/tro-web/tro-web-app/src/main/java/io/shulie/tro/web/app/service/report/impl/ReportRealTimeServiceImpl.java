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

package io.shulie.tro.web.app.service.report.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.pradar.log.parser.trace.RpcEntry;
import com.pamirs.pradar.log.parser.trace.RpcStack;
import com.pamirs.tro.entity.dao.linkmanage.TBusinessLinkManageTableMapper;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTraceDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTraceDetailDTO;
import com.pamirs.tro.entity.domain.entity.linkmanage.BusinessLinkManageTable;
import com.pamirs.tro.entity.domain.entity.linkmanage.figure.RpcType;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageIdVO;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.TraceClient;
import io.shulie.tro.web.amdb.bean.query.trace.TraceInfoQueryDTO;
import io.shulie.tro.web.amdb.bean.result.trace.EntryTraceInfoDTO;
import io.shulie.tro.web.app.service.report.ReportRealTimeService;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.app.service.risk.util.DateUtil;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.PradarWebRequest;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.http.HttpWebClient;
import io.shulie.tro.web.common.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @ClassName ReportRealTimeServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/8/17 下午8:22
 */
@Service
@Slf4j
public class ReportRealTimeServiceImpl implements ReportRealTimeService {

    @Resource
    TBusinessLinkManageTableMapper tBusinessLinkManageTableMapper;
    @Autowired
    private ReportService reportService;
    @Autowired
    private HttpWebClient httpWebClient;
    @Autowired
    private TraceClient traceClient;

    @Override
    public PageInfo<ReportTraceDTO> getReportLinkList(Long sceneId, Long startTime, Integer type, int current,
        int pageSize) {
        if (startTime == null) {
            return new PageInfo<>(Lists.newArrayList());
        }
        return getReportTraceDTOS(sceneId, startTime, System.currentTimeMillis(), type, current, pageSize);
    }

    @Override
    public PageInfo<ReportTraceDTO> getReportLinkListByReportId(Long reportId, Integer type, int current,
        int pageSize) {
        ReportDetailDTO reportDetail = null;
        WebResponse<HashMap> response = reportService.getReportByReportId(reportId);
        if (response != null && response.getData() != null) {
            reportDetail = JSON.parseObject(JSON.toJSONString(response.getData()), ReportDetailDTO.class);
            log.info("Report Id={}, Status={}", reportId, reportDetail.getTaskStatus());
        }
        if (reportDetail == null || reportDetail.getStartTime() == null) {
            return new PageInfo<>(Lists.newArrayList());
        }
        long startTime = DateUtil.parseSecondFormatter(reportDetail.getStartTime()).getTime();
        //如果reportDetail.getEndTime()为空，取值5min,考虑到取当前时间的话，后续可能会查太多数据
        long endTime = reportDetail.getEndTime() != null ? reportDetail.getEndTime().getTime() : startTime + 5 * 60L;
        return getReportTraceDTOS(reportDetail.getSceneId(), startTime, endTime, type, current, pageSize);
    }

    @Override
    public Map<String, Object> getLinkDetail(String traceId, Integer id) {
        PradarWebRequest vo = new PradarWebRequest();
        vo.setHttpMethod(HttpMethod.GET);
        vo.setTraceId(traceId);
        RpcStack rpcStack = traceClient.getTraceDetailById(traceId);
        boolean isTest = !CollectionUtils.isEmpty(rpcStack.getRpcEntries()) && rpcStack.getRpcEntries().get(0)
            .isClusterTest();
        Map<String, Object> map = Maps.newHashMap();
        map.put("startTime", rpcStack.getStartTime());
        map.put("entryHostIp", rpcStack.getRootIp());
        List<ReportTraceDetailDTO> vos = Lists.newArrayList();
        BiMap<Integer, Integer> node = HashBiMap.create();
        AtomicInteger integer = new AtomicInteger(0);
        List<ReportTraceDetailDTO> dto = coverEntriesList(0L, Lists.newArrayList(),
            rpcStack.getRpcEntries(), vos, node, -1, integer);
        List<ReportTraceDetailDTO> result = Lists.newArrayList();
        coverResult(dto, id, result);
        map.put("traces", result);
        map.put("clusterTest", isTest);
        map.put("totalCost", rpcStack.getTotalCost());
        return map;
    }

    private void coverResult(List<ReportTraceDetailDTO> dtoList, Integer id, List<ReportTraceDetailDTO> result) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        if (id == 0) {
            if (dtoList.get(0).getNextNodes() != null) {
                dtoList.get(0).getNextNodes().forEach(dto -> {
                    dto.setNextNodes(
                        dto.getNextNodes() != null && dto.getNextNodes().size() > 0 ? Lists.newArrayList() : null);
                });
            }
            result.addAll(dtoList);
            return;
        }
        for (ReportTraceDetailDTO dto : dtoList) {
            if (CollectionUtils.isEmpty(dto.getNextNodes())) {
                continue;
            }
            if (dto.getId().equals(id)) {
                if (dto.getNextNodes() != null) {
                    result.addAll(dto.getNextNodes());
                }
            }
            coverResult(dto.getNextNodes(), id, result);
        }
    }

    private List<ReportTraceDetailDTO> coverEntriesList(long startTime, List<String> convertedEntriesList,
        List<RpcEntry> rpcEntries, List<ReportTraceDetailDTO> vos,
        BiMap<Integer, Integer> node, Integer id, AtomicInteger integer) {
        if (CollectionUtils.isEmpty(rpcEntries)) {
            return null;
        }
        return rpcEntries.stream().map(rpcEntry -> {
            String convertedEntriesKey = rpcEntry.getAppName() + "|" + rpcEntry.getServiceName() + "|" +
                rpcEntry.getMethodName() + "|" + rpcEntry.getRpcType() + "|" + rpcEntry.getMiddlewareName() +
                "|" + rpcEntry.getRpcId();
            if (convertedEntriesList.contains(convertedEntriesKey)) {
                return null;
            }
            convertedEntriesList.add(convertedEntriesKey);
            ReportTraceDetailDTO reportTraceDetailDTO = new ReportTraceDetailDTO();
            reportTraceDetailDTO.setId(integer.getAndIncrement());
            node.forcePut(id, reportTraceDetailDTO.getId());
            // 原始层级
            reportTraceDetailDTO.setCostTime(rpcEntry.getCost());
            reportTraceDetailDTO.setApplicationName(rpcEntry.getAppName());
            reportTraceDetailDTO.setInterfaceName(rpcEntry.getServiceName());
            long offset = 0L;
            for (RpcEntry entry : rpcEntries) {
                String tempKey = entry.getAppName() + "|" + entry.getServiceName() + "|" +
                    entry.getMethodName() + "|" + entry.getRpcType() + "|" + entry.getMiddlewareName() +
                    "|" + entry.getRpcId();
                if(tempKey.equals(convertedEntriesKey)) {
                    break;
                }
                offset += entry.getCost();
            }
            if ("0".equals(rpcEntry.getRpcId())) {
                reportTraceDetailDTO.setOffsetStartTime(0L);
            } else {
                reportTraceDetailDTO.setOffsetStartTime(startTime + offset);
            }
            reportTraceDetailDTO.setParams(buildParams(rpcEntry));
            reportTraceDetailDTO.setSucceeded(ResultCode.isOk(rpcEntry.getResultCode()));
            reportTraceDetailDTO.setRpcId(rpcEntry.getRpcId());
            reportTraceDetailDTO.setAgentId(rpcEntry.getClientAgentId());
            reportTraceDetailDTO.setEntryHostIp(rpcEntry.getClientIp());
            reportTraceDetailDTO.setClusterTest(rpcEntry.isClusterTest());
            // 客户端IP和服务端IP如果两个都有值那就取服务端IP，如果只有一个有值那就取有值的那个
            if(StringUtils.isNotBlank(rpcEntry.getClientIp()) && StringUtils.isNotBlank(rpcEntry.getServerIp())) {
                reportTraceDetailDTO.setNodeIp(rpcEntry.getServerIp());
            }else if(StringUtils.isNotBlank(rpcEntry.getClientIp())){
                reportTraceDetailDTO.setNodeIp(rpcEntry.getClientIp());
            }else if(StringUtils.isNotBlank(rpcEntry.getServerIp())){
                reportTraceDetailDTO.setNodeIp(rpcEntry.getServerIp());
            }
            reportTraceDetailDTO.setResponse(rpcEntry.getResponse());
            reportTraceDetailDTO.setNodeSuccess(true);
            if (!reportTraceDetailDTO.getSucceeded()) {
                // 向上递归
                setParentNode(vos, node, reportTraceDetailDTO.getId());
            }
            vos.add(reportTraceDetailDTO);
            reportTraceDetailDTO.setNextNodes(coverEntriesList(reportTraceDetailDTO.getOffsetStartTime(), convertedEntriesList,
                    rpcEntry.getRpcEntries(), vos, node,
                    reportTraceDetailDTO.getId(), integer));
            //rpcEntry = null;
            return reportTraceDetailDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void setParentNode(List<ReportTraceDetailDTO> vos, BiMap<Integer, Integer> node, Integer id) {
        // 层级获取
        List<Integer> nodes = Lists.newArrayList();
        while (node.containsValue(id)) {
            id = node.inverse().get(id);
            nodes.add(id);
        }
        if (vos != null && vos.size() > 0) {
            vos.forEach(vo -> {
                if (nodes.contains(vo.getId())) {
                    vo.setNodeSuccess(false);
                }
            });
        }
    }

    private String buildParams(RpcEntry item) {
        if (item.getRpcType() == RpcType.DB.getValue()) {
            if (StringUtils.isNotBlank(item.getServerCallbackMsg())) {
                return item.getServerCallbackMsg() + "\n" + item.getRequest();
            }
            if (StringUtils.isNotBlank(item.getClientCallbackMsg())) {
                return item.getClientCallbackMsg() + "\n" + item.getRequest();
            }
        }
        return item.getRequest();
    }

    private PageInfo<ReportTraceDTO> getReportTraceDTOS(Long sceneId, long startTime, long endTime, Integer type,
        int current, int pageSize) {
        // 查询场景下的业务活动信息
        SceneManageIdVO vo = new SceneManageIdVO();
        vo.setId(sceneId);
        vo.setRequestUrl(RemoteConstant.SCENE_MANAGE_DETAIL_URL);
        vo.setHttpMethod(HttpMethod.GET);
        WebResponse response = httpWebClient.request(vo);
        Map<String, Object> dataMap = (Map<String, Object>)response.getData();
        List<Map> mapList = (List<Map>)dataMap.get("businessActivityConfig");
        List<Long> businessActivityIdList = Lists.newArrayList();
        for (Map map : mapList) {
            businessActivityIdList.add(Long.parseLong(String.valueOf(map.get("businessActivityId"))));
        }
        // 查询入口集合
        List<BusinessLinkManageTable> businessLinkManageTableList =
            tBusinessLinkManageTableMapper.selectBussinessLinkByIdList(businessActivityIdList);
        List<String> entranceList = Lists.newArrayList();
        for (BusinessLinkManageTable businessLinkManageTable : businessLinkManageTableList) {
            String entrance = businessLinkManageTable.getEntrace();
            if (entrance.contains("http")) {
                entrance = entrance.substring(entrance.indexOf("http"));
            }
            entranceList.add(entrance);
        }

        TraceInfoQueryDTO traceInfoQueryDTO = new TraceInfoQueryDTO();
        traceInfoQueryDTO.setStartTime(startTime);
        traceInfoQueryDTO.setEndTime(endTime);
        traceInfoQueryDTO.setType(String.valueOf(type));
        traceInfoQueryDTO.setEntranceList(entranceList);
        traceInfoQueryDTO.setPageNum(current);
        traceInfoQueryDTO.setPageSize(pageSize);
        PagingList<EntryTraceInfoDTO> entryTraceInfoDTOPagingList = traceClient.listEntryTraceInfo(traceInfoQueryDTO);
        if (entryTraceInfoDTOPagingList.isEmpty()) {
            return new PageInfo<>();
        }
        List<ReportTraceDTO> collect = entryTraceInfoDTOPagingList.getList().stream().map(traceInfo -> {
            ReportTraceDTO traceDTO = new ReportTraceDTO();
            traceDTO.setInterfaceName(traceInfo.getEntry());
            traceDTO.setApplicationName(buildAppName(traceInfo));
            //traceDTO.setSucceeded(ResultCode.isOk(traceInfo.getStatus()));
            traceDTO.setTotalRt(traceInfo.getProcessTime());
            traceDTO.setStartTime(new Date(traceInfo.getStartTime()));
            traceDTO.setTraceId(traceInfo.getTraceId());
            return traceDTO;
        }).collect(Collectors.toList());
        PageInfo<ReportTraceDTO> reportTraceDTOPageInfo = new PageInfo<>(collect);
        reportTraceDTOPageInfo.setTotal(entryTraceInfoDTOPagingList.getTotal());
        return reportTraceDTOPageInfo;
    }

    private String buildAppName(EntryTraceInfoDTO tro) {
        StringBuilder builder = new StringBuilder();
        builder.append(tro.getAppName());
        String entry = tro.getEntry();
        if (StringUtils.isNotBlank(entry) && entry.contains("http")) {
            entry = entry.replace("http://", "");
            entry = entry.replace("https://", "");
            if (entry.contains("/")) {
                entry = entry.substring(0, entry.indexOf("/"));
            }
            if (entry.contains(":")) {
                entry = entry.substring(0, entry.indexOf(":"));
            }
            builder.append("(").append(entry).append(")");
        }
        return builder.toString();
    }

}
