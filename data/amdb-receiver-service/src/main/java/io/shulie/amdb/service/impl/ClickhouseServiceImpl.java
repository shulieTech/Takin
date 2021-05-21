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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.shulie.amdb.service.ClickhouseService;
/*
import io.shulie.surge.data.sink.clickhouse.RoundClickhouseDataSource;
*/
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Order(value = 1)
public class ClickhouseServiceImpl implements ClickhouseService, ApplicationRunner {
    @Value("${config.clickhouse.url}")
    private String clickhouseUrl;

    @Value("${config.clickhouse.username}")
    private String clickhouseUserName;

    @Value("${config.clickhouse.password}")
    private String clickhousePassword;

    @Value("${config.clickhouse.enableRound}")
    private boolean enableRound;

    private volatile JdbcTemplate jdbcTemplate;

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    @Override
    public Map<String, Object> queryForMap(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * 查询map
     *
     * @param sql
     * @return
     */
    @Override
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
    @Override
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
    @Override
    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }


    @Override
    public void run(ApplicationArguments args) {
        ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        if (StringUtils.isNotBlank(clickhouseUserName)) {
            clickHouseProperties.setUser(clickhouseUserName);
        }
        if (StringUtils.isNotBlank(clickhousePassword)) {
            clickHouseProperties.setPassword(clickhousePassword);
        }
        DataSource clickHouseDataSource = null;
        /*if (enableRound) {
            clickHouseDataSource = new RoundClickhouseDataSource(clickhouseUrl, clickHouseProperties);
        } else {*/
            clickHouseDataSource = new BalancedClickhouseDataSource(clickhouseUrl, clickHouseProperties);//.}
        jdbcTemplate = new JdbcTemplate(clickHouseDataSource);
    }
}
