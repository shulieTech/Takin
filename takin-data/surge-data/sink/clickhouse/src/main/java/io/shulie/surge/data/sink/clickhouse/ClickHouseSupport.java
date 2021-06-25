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

package io.shulie.surge.data.sink.clickhouse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.batch.CountRotationPolicy;
import io.shulie.surge.data.common.batch.RotationBatch;
import io.shulie.surge.data.common.batch.TimedRotationPolicy;
import io.shulie.surge.data.common.lifecycle.Lifecycle;
import io.shulie.surge.data.common.lifecycle.Stoppable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 基于 clickhouse 的异步实现
 *
 * @author pamirs
 */
public class ClickHouseSupport implements Lifecycle, Stoppable {
    private static final Logger logger = LoggerFactory.getLogger(ClickHouseSupport.class);

    private DataSource clickHouseDataSource;
    private JdbcTemplate jdbcTemplate;
    private RotationBatch<String> rotationSqlBatch;
    private Map<String, RotationBatch<Object[]>> rotationPrepareSqlBatch = Maps.newHashMap();

    private int batchCount;

    @Inject
    public ClickHouseSupport(@Named("config.clickhouse.url") String url,
                             @Named("config.clickhouse.userName") String username,
                             @Named("config.clickhouse.password") String password,
                             @Named("config.clickhouse.batchCount") int batchCount,
                             @Named("config.clickhouse.enableRound") boolean enableRound) {
        try {
            ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
            if (StringUtils.isNotBlank(username)) {
                clickHouseProperties.setUser(username);
            }
            if (StringUtils.isNotBlank(password)) {
                clickHouseProperties.setPassword(password);
            }
            if (enableRound) {
                clickHouseDataSource = new RoundClickhouseDataSource(url, clickHouseProperties);
            } else {
                clickHouseDataSource = new BalancedClickhouseDataSource(url, clickHouseProperties);
            }
            jdbcTemplate = new JdbcTemplate(clickHouseDataSource);
            this.batchCount = batchCount;
        } catch (Exception e) {
            logger.error("Init datasource failed.", e);
            throw e;
        }
    }

    @Override
    public void start() throws Exception {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(clickHouseDataSource);
        }
        rotationSqlBatch = new RotationBatch(new CountRotationPolicy(batchCount), new TimedRotationPolicy(2, TimeUnit.SECONDS));
        rotationSqlBatch.batchSaver(new RotationBatch.BatchSaver<String>() {
            @Override
            public boolean saveBatch(LinkedBlockingQueue<String> batchSql) {
                jdbcTemplate.batchUpdate(batchSql.toArray(new String[batchSql.size()]));
                return true;
            }
        });
        rotationPrepareSqlBatch = Maps.newHashMap();
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    /**
     * 批量更新
     *
     * @param sql
     */
    public void batchUpdate(final String... sql) {
        for (String s : sql) {
            rotationSqlBatch.addBatch(s);
        }
    }

    /**
     * 批量更新
     *
     * @param sql
     * @param batchArgs
     */
    public void batchUpdate(final String sql, final List<Object[]> batchArgs) {
        RotationBatch<Object[]> rotationBatch = null;
        if (!rotationPrepareSqlBatch.containsKey(sql)) {
            rotationBatch = new RotationBatch(new CountRotationPolicy(batchCount), new TimedRotationPolicy(2, TimeUnit.SECONDS));
            rotationBatch.batchSaver(new RotationBatch.BatchSaver<Object[]>() {
                @Override
                public boolean saveBatch(LinkedBlockingQueue<Object[]> batchSql) {
                    jdbcTemplate.batchUpdate(sql, Lists.newArrayList(batchSql));
                    return true;
                }
            });
        } else {
            rotationBatch = rotationPrepareSqlBatch.get(sql);
        }
        for (Object[] args : batchArgs) {
            rotationBatch.addBatch(args);
        }
        rotationPrepareSqlBatch.put(sql, rotationBatch);
    }

    public void update(final String sql, final List<Object[]> batchArgs) {
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    public Map<String, Object> queryForMap(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    public <T> T queryForObject(String sql, Class<T> clazz) {
        Map<String, Object> result = queryForMap(sql);
        return JSONObject.parseObject(JSON.toJSON(result).toString(), clazz);
    }

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        List<Map<String, Object>> resultList = queryForList(sql);
        if (resultList == null) {
            return null;
        }
        if (resultList.size() == 0) {
            return new ArrayList<>();
        }
        return resultList.stream().map(result -> JSONObject.parseObject(JSON.toJSON(result).toString(), clazz)).collect(Collectors.toList());
    }


    /**
     * 查询list
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
