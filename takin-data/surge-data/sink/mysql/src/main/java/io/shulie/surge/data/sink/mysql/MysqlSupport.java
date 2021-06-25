
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

package io.shulie.surge.data.sink.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.batch.CountRotationPolicy;
import io.shulie.surge.data.common.batch.RotationBatch;
import io.shulie.surge.data.common.batch.TimedRotationPolicy;
import io.shulie.surge.data.common.lifecycle.Lifecycle;
import io.shulie.surge.data.common.lifecycle.Stoppable;
import io.shulie.surge.data.common.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 基于 clickhouse 的异步实现
 *
 * @author pamirs
 */

public class MysqlSupport implements Lifecycle, Stoppable {

    private static final Logger logger = LoggerFactory.getLogger(MysqlSupport.class);

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private RotationBatch<String> rotationSqlBatch;
    private Map<String, RotationBatch<Object[]>> rotationPrepareSqlBatch = Maps.newHashMap();
    private DataSourceTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    public MysqlSupport(@Named("config.mysql.url") String url,
                        @Named("config.mysql.userName") String username,
                        @Named("config.mysql.password") String password,
                        @Named("config.mysql.initialSize") Integer initialSize,
                        @Named("config.mysql.minIdle") Integer minIdle,
                        @Named("config.mysql.maxActive") Integer maxActive) {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setInitialSize(minIdle);
            dataSource.setMinIdle(initialSize);
            dataSource.setMaxActive(maxActive);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setTestWhileIdle(true);
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setMinEvictableIdleTimeMillis(300000);
            dataSource.setMaxWait(60000);
            dataSource.setValidationQuery("select '*'");
            this.dataSource = dataSource;
            jdbcTemplate = new JdbcTemplate(dataSource);
            transactionManager = new DataSourceTransactionManager(dataSource);
            transactionTemplate = new TransactionTemplate(transactionManager);
        } catch (Exception e) {
            logger.error("Init datasource failed.", e);
            throw e;
        }
    }

    @Override
    public void start() throws Exception {
        if (jdbcTemplate != null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        rotationSqlBatch = new RotationBatch(new CountRotationPolicy(200), new TimedRotationPolicy(2, TimeUnit.SECONDS));
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
            rotationBatch = new RotationBatch(new CountRotationPolicy(200), new TimedRotationPolicy(2, TimeUnit.SECONDS));
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

    public void updateBatch(final String sql, final List<Object[]> batchArgs) {
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void update(final String sql, final Object[] args) {
        jdbcTemplate.update(sql, args);
    }

    public void updateTrans(List<Pair<String, Object[]>> sqlInTrans) {
        //final String sql, final Object[] args
        transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                Object savepoint = transactionStatus.createSavepoint();
                // DML执行
                try {
                    for (Pair<String, Object[]> pair : sqlInTrans) {
                        int rs = jdbcTemplate.update(pair.getFirst(), pair.getSecond());
                    }
                } catch (Throwable e) {
                    logger.error("Error occured, cause by: {}", e.getMessage());
                    transactionStatus.setRollbackOnly();
                    // transactionStatus.rollbackToSavepoint(savepoint);
                }
                return null;
            }
        });
    }


    public void updateTransOnlySql(List<String> sqls) {
        //final String sql, final Object[] args
        transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                Object savepoint = transactionStatus.createSavepoint();
                // DML执行
                try {
                    for (String sql : sqls) {
                        int rs = jdbcTemplate.update(sql);
                    }
                } catch (Throwable e) {
                    logger.error("Error occured, cause by: {}", e.getMessage());
                    transactionStatus.setRollbackOnly();
                    // transactionStatus.rollbackToSavepoint(savepoint);
                }
                return null;
            }
        });
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
        return jdbcTemplate.queryForObject(sql, clazz);
    }

    /**
     * 查询map
     *
     * @param sql
     * @return
     */

    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        return jdbcTemplate.queryForList(sql, clazz);
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
