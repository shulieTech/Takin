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

import io.shulie.amdb.request.query.MetricsQueryRequest;
import io.shulie.amdb.response.metrics.MetricsResponse;
import io.shulie.amdb.service.MetricsService;
import io.shulie.amdb.utils.InfluxDBManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class MetricsServiceImpl implements MetricsService {

    @Autowired
    private InfluxDBManager influxDBManager;

    @Override
    public Map<String, MetricsResponse> getMetrics(MetricsQueryRequest request) {
        Map<String, MetricsResponse> responseList = new HashMap<>();
        request.getTagMapList().forEach(tagMap -> {
            String sql = "select " + parseAliasFields(request.getFieldMap()) +
                    " from  " + request.getMeasurementName() + " where " + parseWhereFilter(tagMap) + " and time >= " +
                    formatTimestamp(request.getStartTime()) + " and time < " + formatTimestamp(request.getEndTime()) + " "+
                    parseGroupBy(request.getGroups());
            List<QueryResult.Result> queryResult = influxDBManager.query(sql);
            queryResult.stream().filter((internalResult) ->
                    Objects.nonNull(internalResult) && Objects.nonNull(internalResult.getSeries()))
                    .forEach((internalResult) -> {
                internalResult.getSeries().stream().filter((series) ->
                        series.getName().equals(request.getMeasurementName()))
                        .forEachOrdered((series) -> {
                    LinkedHashMap<String, String> resultTagMap = new LinkedHashMap<>();
                    resultTagMap.putAll(tagMap);
                    List<Map<String, Object>> resultList = new ArrayList<>();
                    if(MapUtils.isNotEmpty(series.getTags())){
                        String[] groupFields = request.getGroups().split(",");
                        for(String groupField:groupFields){
                            resultTagMap.put(groupField,series.getTags().get(groupField));
                        }
                    }
                    Iterator iterable = series.getValues().iterator();
                    while (iterable.hasNext()) {
                        Map<String, Object> result = new HashMap<>();
                        List<Object> row = (List) iterable.next();
                        for (int i = 0; i < series.getColumns().size(); i++) {
                            String column = series.getColumns().get(i);
                            result.put(column, row.get(i));
                        }
                        resultList.add(result);
                    }
                    MetricsResponse response = new MetricsResponse();
                    response.setTags(resultTagMap);
                    response.setValue(resultList);
                    response.setTimes(request.getEndTime() - request.getStartTime());
                    log.debug("指标查询sql:{}", sql);
                    log.debug("查询结果:{}", response);
                    responseList.put(StringUtils.join(resultTagMap.values(), "|"), response);
                });
            });
        });

        log.debug("指标查询合并结果:{}", responseList);
        return responseList;
    }

    /**
     * 处理查询列
     *
     * @param fieldsMap
     * @return
     */
    private String parseAliasFields(Map<String, String> fieldsMap) {
        List<String> aliasList = new ArrayList<>();
        fieldsMap.forEach((k, v) -> {
            aliasList.add(v + " as " + k);
        });
        return StringUtils.join(aliasList, ",");
    }

    /**
     * 处理查询条件
     *
     * @param tagMap
     * @return
     */
    private String parseWhereFilter(Map<String, String> tagMap) {
        List<String> filterList = new ArrayList<>();
        tagMap.forEach((k, v) -> {
            filterList.add(k + "='" + v + "'");
        });
        return StringUtils.join(filterList, " and ");
    }

    /**
     * 处理分组条件
     *
     * @param groupFields
     * @return
     */
    private String parseGroupBy(String groupFields) {
        if (StringUtils.isBlank(groupFields)) {
            return "";
        }
        return "group by " + groupFields;
    }

    /**
     * 时间格式化
     *
     * @param timestamp
     * @return
     */
    private long formatTimestamp(long timestamp) {
        String temp = timestamp + "000000";
        return Long.valueOf(temp);
    }

    /**
     * 浮点数据格式化
     *
     * @param data
     * @return
     */
    private BigDecimal formatDouble(Double data) {
        if (data == null) {
            return new BigDecimal("0");
        }
        BigDecimal b = BigDecimal.valueOf(data);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
