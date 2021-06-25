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

package io.shulie.surge.data.sink.influxdb;

import io.shulie.surge.data.common.pool.NamedThreadFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于 InfluxDBSupport
 *
 * @author pamirs
 */
public class DefaultInfluxDBSupport implements InfluxDBSupport {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInfluxDBSupport.class);

    private InfluxDB influxDB;

    public DefaultInfluxDBSupport(String url, String username, String password) {
        try {
            influxDB = InfluxDBFactory.connect(url, username, password);

            influxDB.enableBatch(BatchOptions.DEFAULTS.actions(1000).flushDuration(500).bufferLimit(5000).exceptionHandler(((points, e) ->
            {
                List<Point> target = new ArrayList<>();
                points.forEach(target::add);
                logger.error("failed to write points:{}",target, e);
            })).threadFactory(new NamedThreadFactory("influx-flush", true)));
        } catch (Throwable e) {
            logger.error("influxDB init fail");
            throw e;
        }
    }

    /**
     * 批量写入
     *
     * @param batchPoints 批量数据点
     */
    @Override
    public void writeBatchPoint(BatchPoints batchPoints) {
        influxDB.write(batchPoints);
    }

    @Override
    public BatchPoints getBatchPoints(String dataBase) {
        BatchPoints build = BatchPoints.database(dataBase).build();
        return build;
    }

    /**
     * 插入数据
     *
     * @param measurement 表名
     * @param tags        tag 带索引
     * @param fields      field 不带
     */
    @Override
    public void write(String dataBase, String measurement, Map<String, String> tags, Map<String, Object> fields, long time) {
        Point.Builder builder = Point.measurement(measurement);
        if (tags.isEmpty() && fields.isEmpty()) {
            throw new RuntimeException("tags is null and field is null");
        }
        if (!tags.isEmpty()) {
            builder.tag(tags);
        }
        if (!fields.isEmpty()) {
            builder.fields(fields);
        }
        if (time > 0) {
            builder.time(time, TimeUnit.MILLISECONDS);
        }
        try {
            influxDB.write(dataBase, "", builder.build());
        } catch (Throwable e) {
            logger.error("write influxDB error" + ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    /**
     * 创建默认保留策略
     *
     * @param policyName  策略名
     * @param duration    保存天数
     * @param replication 保存副本数量
     */
    @Override
    public void createRetentionPolicy(String dataBase, String policyName, String duration, int replication) {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                policyName, dataBase, duration + "d", replication);
        influxDB.query(new Query(command, dataBase));
    }

    @Override
    public void stop() {
        try {
            if (influxDB != null) {
                influxDB.close();
            }
        } catch (Throwable e) {
            logger.error("close influxDB fail");
        }
    }

    /**
     * 检查database是否存在。不存在则创建
     *
     * @param dataBase
     */
    @Override
    public void createDatabase(String dataBase) {
        if (StringUtils.isBlank(dataBase)) {
            return;
        }
        List<List<Object>> list = parseSingleRecord("SHOW DATABASES");
        if (CollectionUtils.isEmpty(list)) {
            influxDB.query(new Query("CREATE DATABASE " + dataBase));
        } else {
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                List<Object> tmpList = list.get(i);
                for (int j = 0; j < tmpList.size(); j++) {
                    if (dataBase.equals(Objects.toString(tmpList.get(j)))) {
                        count++;
                    }
                }
            }
            if (count == 0) {
                influxDB.query(new Query("CREATE DATABASE " + dataBase));
            }
        }
    }

    private List<List<Object>> parseSingleRecord(String command) {
        QueryResult queryResult = influxDB.query(new Query(command));
        List<QueryResult.Result> results = queryResult.getResults();
        if (results == null || results.size() == 0) {
            return null;
        }
        List<QueryResult.Series> series = results.get(0).getSeries();
        if (series == null || series.size() == 0) {
            return null;
        }
        QueryResult.Series obj = series.get(0);
        List<List<Object>> values = obj.getValues();
        if (values == null || values.size() == 0) {
            return null;
        }
        return values;
    }
}
