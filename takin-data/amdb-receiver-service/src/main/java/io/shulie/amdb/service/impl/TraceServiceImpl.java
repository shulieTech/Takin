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

package io.shulie.amdb.service.impl;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.common.dto.trace.EntryTraceInfoDTO;
import io.shulie.amdb.common.enums.RpcType;
import io.shulie.amdb.common.request.trace.EntryTraceQueryParam;
import io.shulie.amdb.constant.PradarLogType;
import io.shulie.amdb.entity.TTrackClickhouseModel;
import io.shulie.amdb.enums.MiddlewareType;
import io.shulie.amdb.service.ClickhouseService;
import io.shulie.amdb.service.TraceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TraceServiceImpl implements TraceService {
    // trace日志字段
    private static final String RPC_BASED_SQL = " appName,traceId,level,parentIndex,index,rpcId,rpcType,logType,traceAppName,upAppName,startTime,cost,middlewareName,serviceName,methodName,remoteIp,port,resultCode,request,response,clusterTest,callbackMsg,attributes,localAttributes,async,version,hostIp,agentId ";

    @Autowired
    ClickhouseService clickhouseService;

    @Override
    public Response<List<EntryTraceInfoDTO>> getEntryTraceInfo(EntryTraceQueryParam param) {
        List<EntryTraceInfoDTO> entryTraceInfoDTOS = new ArrayList<>();
        // 拼装查询字段
        List<String> selectFields = new ArrayList<>();
        if (StringUtils.isBlank(param.getAppName()) && StringUtils.isBlank(param.getEntranceList())) {
            return Response.fail("参数错误，未指定查询字段");
        }
        if (StringUtils.isBlank(param.getFieldNames())) {
            return Response.fail("参数错误，未指定查询结果字段列表");
        } else {
            List<String> fieldNames = Arrays.asList(param.getFieldNames().split(","));
            if (fieldNames.contains("appName")) {
                selectFields.add("appName");
            }
            if (fieldNames.contains("remoteIp")) {
                selectFields.add("remoteIp");
            }
            if (fieldNames.contains("port")) {
                selectFields.add("port");
            }
            if (fieldNames.contains("resultCode")) {
                selectFields.add("resultCode");
            }
            if (fieldNames.contains("rpcType")) {
                selectFields.add("rpcType");
            }
            if (fieldNames.contains("cost")) {
                selectFields.add("cost");
            }
            if (fieldNames.contains("startTime")) {
                selectFields.add("startTime");
            }
            if (fieldNames.contains("traceId")) {
                selectFields.add("traceId");
            }
            if (fieldNames.contains("serviceName")) {
                selectFields.add("parsedServiceName as serviceName");
            }
            if (fieldNames.contains("methodName")) {
                selectFields.add("parsedMethod as methodName");
            }
        }
        // 拼装过滤条件
        List<String> andFilterList = new ArrayList<>();
        List<String> orFilterList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getAppName())) {
            andFilterList.add("appName='" + param.getAppName() + "'");
        }
        if (StringUtils.isNotBlank(param.getRpcType())) {
            // web server
            if (param.getRpcType().equals(RpcType.TYPE_WEB_SERVER + "")) {
                andFilterList.add("rpcType='" + RpcType.TYPE_WEB_SERVER + "'");
            }
            // dubbo 或者 MQ 都取服务端日志
            else if (param.getRpcType().equals(RpcType.TYPE_RPC + "") || param.getRpcType().equals(
                    RpcType.TYPE_MQ + "")) {
                andFilterList.add("rpcType='" + RpcType.TYPE_WEB_SERVER + "'");
                andFilterList.add("logType='" + PradarLogType.LOG_TYPE_RPC_SERVER + "'");
            }
            // 其他类型暂不支持
            else {
                andFilterList.add("1 = -1");
            }
        } else {
            // 如果不传rpcType，则查询所有服务端
            andFilterList.add("(logType='1' or logType='3')");
        }
        if (StringUtils.isNotBlank(param.getMethodName())) {
            andFilterList.add("parsedMethod='" + param.getMethodName() + "'");
        }
        if (StringUtils.isNotBlank(param.getServiceName())) {
            andFilterList.add("parsedServiceName='" + param.getServiceName() + "'");
        }
        if (StringUtils.isNotBlank(param.getEntranceList())) {
            List<String> entryList = Arrays.asList(param.getEntranceList().split(","));
            entryList.forEach(entrance -> {
                String[] entranceInfo = entrance.split("#");
                if (StringUtils.isNotBlank(entranceInfo[0])) {
                    orFilterList.add("(appName='" + entranceInfo[0] + "' and parsedServiceName='" + entranceInfo[1]
                            + "' and parsedMethod='" + entranceInfo[2] + "' and rpcType='" + entranceInfo[3] + "')");
                }
            });
        }
        if (StringUtils.isNotBlank(param.getResultType())) {
            if (param.getResultType().equals("1")) {
                andFilterList.add("(resultCode='00' or resultCode='200')");
            }
            if (param.getResultType().equals("0")) {
                andFilterList.add("(resultCode<>'00' and resultCode<>'200')");
            }
        }
        if (StringUtils.isNotBlank(param.getClusterTest())) {
            andFilterList.add("clusterTest='" + param.getClusterTest() + "'");
        }
        if (param.getStartTime() != null && param.getStartTime() > 0) {
            andFilterList.add("startDate >= '" + DateFormatUtils.format(new Date(param.getStartTime()), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (param.getEndTime() != null && param.getEndTime() > 0) {
            andFilterList.add("startDate <= '" + DateFormatUtils.format(new Date(param.getEndTime()), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (CollectionUtils.isEmpty(andFilterList)) {
            return Response.fail("参数错误，必须参数没有传值");
        }
        //        // 时间过滤，从昨天0点开始到当前时间
        //        long queryTime = System.currentTimeMillis() / 1000 / 60 / 60 / 24 - 1;
        //        filterList.add("dateToMin >= '" + queryTime + "'");
        // 分页
        String limit = "";
        if ((param.getPageSize() != null && param.getPageSize() > 0) || (param.getCurrentPage() != null
                && param.getCurrentPage() > 0)) {
            int pageSize = param.getPageSize();
            if (pageSize <= 0) {
                pageSize = 20;
            }
            int currentPage = param.getCurrentPage();
            if (currentPage <= 0) {
                currentPage = 1;
            }
            limit = "limit " + ((currentPage - 1) * pageSize) + "," + pageSize;
        }
        String sql = "select " + StringUtils.join(selectFields, ",") + " from t_trace_all where " + StringUtils.join(
                andFilterList, " and ");
        if (CollectionUtils.isNotEmpty(orFilterList)) {
            sql += " and (" + StringUtils.join(orFilterList, " or ") + ")";
        }
        String countSql = "select count(1) as total " + " from t_trace_all where " + StringUtils.join(andFilterList,
                " and ");
        if (CollectionUtils.isNotEmpty(orFilterList)) {
            countSql += " and (" + StringUtils.join(orFilterList, " or ") + ")";
        }
        sql += " order by traceId desc " + limit;
        List<Map<String, Object>> dataMapList = clickhouseService.queryForList(sql);
        if (CollectionUtils.isNotEmpty(dataMapList)) {
            dataMapList.forEach(dataMap -> {
                String serviceName = objectToString(dataMap.get("serviceName"), null);
                String methodName = objectToString(dataMap.get("methodName"), null);
                String appName = objectToString(dataMap.get("appName"), null);
                String remoteIp = objectToString(dataMap.get("remoteIp"), null);
                String port = objectToString(dataMap.get("port"), null);
                String resultCode = objectToString(dataMap.get("resultCode"), null);
                String cost = objectToString(dataMap.get("cost"), null);
                String startTime = objectToString(dataMap.get("startTime"), null);
                String traceId = objectToString(dataMap.get("traceId"), null);

                EntryTraceInfoDTO entryTraceInfoDTO = new EntryTraceInfoDTO();
                entryTraceInfoDTO.setServiceName(serviceName);
                entryTraceInfoDTO.setMethodName(methodName);
                entryTraceInfoDTO.setAppName(appName);
                entryTraceInfoDTO.setRemoteIp(remoteIp);
                entryTraceInfoDTO.setPort(port);
                entryTraceInfoDTO.setResultCode(resultCode);
                entryTraceInfoDTO.setCost(NumberUtils.toLong(cost, 0));
                entryTraceInfoDTO.setStartTime(NumberUtils.toLong(startTime, 0));
                entryTraceInfoDTO.setTraceId(traceId);
                entryTraceInfoDTOS.add(entryTraceInfoDTO);
            });
        }
        Map<String, Object> countInfo = clickhouseService.queryForMap(countSql);
        long total = NumberUtils.toLong("" + countInfo.get("total"), 0);
        Response result = Response.success(entryTraceInfoDTOS);
        result.setTotal(total);
        return result;
    }


    @Override
    public Response<Map<String, List<RpcBased>>> getTraceInfo(EntryTraceQueryParam param) {

        // 拼装过滤条件
        List<String> andFilterList = new ArrayList<>();
        List<String> orFilterList = new ArrayList<>();

        if (StringUtils.isNotBlank(param.getAppName())) {
            andFilterList.add("appName='" + param.getAppName() + "'");
        }
        if (StringUtils.isNotBlank(param.getMethodName())) {
            andFilterList.add("parsedMethod='" + param.getMethodName() + "'");
        }
        if (StringUtils.isNotBlank(param.getServiceName())) {
            andFilterList.add("parsedServiceName='" + param.getServiceName() + "'");
        }
        if (StringUtils.isNotBlank(param.getEntranceList())) {
            List<String> entryList = Arrays.asList(param.getEntranceList().split(","));
            entryList.forEach(entrance -> {
                String[] entranceInfo = entrance.split("#");
                if (StringUtils.isNotBlank(entranceInfo[0])) {
                    orFilterList.add("(appName='" + entranceInfo[0] + "' and parsedServiceName='" + entranceInfo[1]
                            + "' and parsedMethod='" + entranceInfo[2] + "' and rpcType='" + entranceInfo[3] + "')");
                }
            });
        }
        if (StringUtils.isNotBlank(param.getResultType())) {
            if (param.getResultType().equals("1")) {
                andFilterList.add("(resultCode='00' or resultCode='200')");
            }
            if (param.getResultType().equals("0")) {
                andFilterList.add("(resultCode<>'00' and resultCode<>'200')");
            }
        }
        if (StringUtils.isNotBlank(param.getClusterTest())) {
            andFilterList.add("clusterTest='" + param.getClusterTest() + "'");
        }
        if (param.getStartTime() != null && param.getStartTime() > 0) {
            andFilterList.add("startDate >= '" + DateFormatUtils.format(new Date(param.getStartTime()), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (param.getEndTime() != null && param.getEndTime() > 0) {
            andFilterList.add("startDate <= '" + DateFormatUtils.format(new Date(param.getEndTime()), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        //        // 时间过滤，从昨天0点开始到当前时间
        //        long queryTime = System.currentTimeMillis() / 1000 / 60 / 60 / 24 - 1;
        //        filterList.add("dateToMin >= '" + queryTime + "'");
        // 分页
        String limit = "";
        if ((param.getPageSize() != null && param.getPageSize() > 0) || (param.getCurrentPage() != null
                && param.getCurrentPage() > 0)) {
            int pageSize = param.getPageSize();
            if (pageSize <= 0) {
                pageSize = 20;
            }
            int currentPage = param.getCurrentPage();
            if (currentPage <= 0) {
                currentPage = 1;
            }
            limit = "limit " + ((currentPage - 1) * pageSize) + "," + pageSize;
        } else {
            limit = "limit 10";
        }
        String sql = "select traceId from t_trace_all where " + StringUtils.join(
                andFilterList, " and ");
        if (CollectionUtils.isNotEmpty(orFilterList)) {
            sql += " and (" + StringUtils.join(orFilterList, " or ") + ")";
        }
        String countSql = "select count(1) as total " + " from t_trace_all where " + StringUtils.join(andFilterList,
                " and ");
        if (CollectionUtils.isNotEmpty(orFilterList)) {
            countSql += " and (" + StringUtils.join(orFilterList, " or ") + ")";
        }
        sql += " order by traceId desc " + limit;
        List<Map<String, Object>> modelList = clickhouseService.queryForList(sql);
        Map<String, List<RpcBased>> traceMap = new HashMap<>();
        for (Map<String, Object> map : modelList) {
            String traceId = String.valueOf(map.get("traceId"));
            traceMap.put(traceId, getTraceDetail(traceId));
        }
        Map<String, Object> countInfo = clickhouseService.queryForMap(countSql);
        long total = NumberUtils.toLong("" + countInfo.get("total"), 0);
        Response result = Response.success(traceMap);
        result.setTotal(total);
        return result;
    }


    @Override
    public List<RpcBased> getTraceDetail(String traceId) {
        String sql = "select " + RPC_BASED_SQL + " from t_trace_all where traceId='" + traceId + "' order by rpcId limit 500";
        List<TTrackClickhouseModel> modelList = clickhouseService.queryForList(sql, TTrackClickhouseModel.class);
        for (TTrackClickhouseModel model : modelList) {
            //jdk-http客户端日志，重新计算耗时
            //if (model.getMiddlewareName().toLowerCase().equals("jdk-http") && model.getLogType() == 2) {
            //calculateCost(model, modelList);
            //}
            //所有客户端都要重新计算耗时
            if (model.getLogType() == 2) {
                calculateCost(model, modelList);
            }
        }
        List<RpcBased> rpcBasedList = modelList.stream().map(model -> model.getRpcBased()).collect(Collectors.toList());
        return rpcBasedList;
    }

    private void calculateCost(TTrackClickhouseModel clientModel, List<TTrackClickhouseModel> modelList) {
        TTrackClickhouseModel serverModel = modelList.stream().filter(m -> m.getLogType() == 3 && m.getRpcId().startsWith(clientModel.getRpcId()) && m.getServiceName().equals(clientModel.getServiceName()) && m.getMethodName().equals(clientModel.getMethodName())).findFirst().orElse(null);
        if (serverModel != null) {
            // 非MQ的中间件，如果客户端耗时小于服务端耗时，则客户端耗时=客户端耗时+服务端耗时
            if (clientModel.getRpcType() != MiddlewareType.TYPE_MQ) {
                if (clientModel.getCost() < serverModel.getCost()) {
                    clientModel.setCost(clientModel.getCost() + serverModel.getCost());
                }
            }
        }
    }

    /**
     * 对象转字符串
     *
     * @param value
     * @param defaultStr
     * @return
     */
    private String objectToString(Object value, String defaultStr) {
        if (value == null || "null".equals(value.toString().toLowerCase())) {
            return defaultStr;
        }
        return ObjectUtils.toString(value);
    }
}
