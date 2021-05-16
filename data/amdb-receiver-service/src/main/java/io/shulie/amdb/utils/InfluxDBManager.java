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

package io.shulie.amdb.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @Author: xingchen
 * @ClassName: InfluxDBManager
 * @Package: io.shulie.tro.report.service
 * @Date: 2020/7/2717:03
 * @Description:
 */
@Service
public class InfluxDBManager implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(InfluxDBManager.class);

    private static final ThreadLocal<InfluxDB> cache = new ThreadLocal<>();
    private static final InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();

    @Value("${influx.openurl}")
    private String influxdbUrl;
    @Value("${influx.username}")
    private String userName;
    @Value("${influx.password}")
    private String password;
    @Value("${influx.database}")
    private String database;

    private InfluxDB createInfluxDB() {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(influxdbUrl, userName, password);
            return influxDB;
        } catch (Throwable e) {
            logger.error("influxdb init fail " + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private InfluxDB getInfluxDB() {
        InfluxDB influxDB = cache.get();
        if (influxDB == null) {
            influxDB = createInfluxDB();
            cache.set(influxDB);
        }
        return influxDB;
    }

    public <T> Collection<T> query(Class<T> clazz, String command) {
        return query(clazz, new Query(command, database));
    }

    public <T> Collection<T> query(Class<T> clazz, String command, String database) {
        return query(clazz, new Query(command, database));
    }


    public List<QueryResult.Result> query(String command) {
        return parseRecords(command);
    }

    public <T> Collection<T> query(Class<T> clazz, Query query) {
        InfluxDB influxDB = getInfluxDB();
        if (influxDB != null) {
            QueryResult queryResult = influxDB.query(query);
            if (queryResult != null && CollectionUtils.isNotEmpty(queryResult.getResults())) {
                return resultMapper.toPOJO(queryResult, clazz);
            }
        }
        return null;
    }

    public <T> Collection<T> query(Class<T> clazz, Query query, String measurementName) {
        InfluxDB influxDB = getInfluxDB();
        if (influxDB != null) {
            QueryResult queryResult = influxDB.query(query);
            if (queryResult != null && CollectionUtils.isNotEmpty(queryResult.getResults())) {
                return resultMapper.toPOJO(queryResult, clazz, measurementName);
            }
        }
        return null;
    }

    @Override
    public void close() {
        InfluxDB influxDB = cache.get();
        if (influxDB != null) {
            influxDB.close();
        }
    }

    private List<QueryResult.Result> parseRecords(String command) {
        QueryResult queryResult = getInfluxDB().query(new Query(command, database));
        List<QueryResult.Result> results = queryResult.getResults();
        if (results == null || results.size() == 0) {
            return null;
        }
        return results;
    }
}
