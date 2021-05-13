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

package io.shulie.tro.web.app.service.elasticjoblite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.VerifyResultStatusEnum;
import com.pamirs.tro.common.constant.VerifyTypeEnum;
import com.pamirs.tro.common.util.JdbcConnection;
import com.pamirs.tro.common.util.SpringUtil;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskJobParameter;
import io.shulie.tro.web.app.request.leakverify.VerifyTaskConfig;
import io.shulie.tro.web.app.service.VerifyTaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: fanxx
 * @Date: 2021/1/7 8:31 下午
 * @Description:
 */
public class VerifyJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(VerifyJob.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("定时任务开始检测:[{}],当前时间:[{}],定时任务ID:[{}]", shardingContext.getJobName(), new Date(),
            shardingContext.getTaskId());
        String jobParameterString = shardingContext.getJobParameter();
        LeakVerifyTaskJobParameter jobParameter = JSON.parseObject(jobParameterString,
            LeakVerifyTaskJobParameter.class);
        Integer refType = jobParameter.getRefType();
        Long refId = jobParameter.getRefId();
        VerifyTypeEnum typeEnum = VerifyTypeEnum.getTypeByCode(refType);

        logger.info("开始执行验证任务[refType:{},refId:{}]", Objects.requireNonNull(typeEnum).name(), refId);
        logger.info(shardingContext.toString());
        List<VerifyTaskConfig> taskConfigs = jobParameter.getVerifyTaskConfigList();
        Map<Integer, Integer> resultMap = verify(refType, refId, taskConfigs);

        logger.info("验证任务已完成，验证结果入库:[refType:{},refId:{}]", typeEnum.name(), refId);
        VerifyTaskService verifyTaskService = (VerifyTaskService)SpringUtil.getBean("verifyTaskServiceImpl");
        verifyTaskService.saveVerifyResult(jobParameter, resultMap);
    }

    public Map<Integer, Integer> run(LeakVerifyTaskJobParameter jobParameter) {
        Integer refType = jobParameter.getRefType();
        Long refId = jobParameter.getRefId();
        VerifyTypeEnum typeEnum = VerifyTypeEnum.getTypeByCode(refType);

        logger.info("开始运行验证任务[refType:{},refId:{}]", Objects.requireNonNull(typeEnum).name(), refId);
        logger.info("任务参数:[{}]", JSON.toJSONString(jobParameter));
        List<VerifyTaskConfig> taskConfigs = jobParameter.getVerifyTaskConfigList();
        return verify(refType, refId, taskConfigs);
    }

    private Map<Integer, Integer> verify(Integer refType, Long refId, List<VerifyTaskConfig> taskConfigs) {
        Map<Integer, Integer> resultMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(taskConfigs)) {
            taskConfigs.forEach(taskConfig -> {
                String jdbcUrl = taskConfig.getJdbcUrl();
                String username = taskConfig.getUsername();
                String password = taskConfig.getPassword();
                Long datasourceId = taskConfig.getDatasourceId();
                List<String> sqls = taskConfig.getSqls();
                Connection connection = null;
                try {
                    connection = JdbcConnection.generateConnection(jdbcUrl, username, password);
                    if (Objects.isNull(connection)) {
                        throw new TroWebException(ExceptionCode.VERIFY_TASK_RUN_FAILED, "获取数据库连接失败");
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    logger.error("Verify job run error:", e);
                }
                try {
                    Connection finalConnection = connection;
                    sqls.forEach(sql -> {
                        String keyString = refType + refId + datasourceId + sql;
                        Integer key = keyString.hashCode();
                        try {
                            Statement statement = finalConnection.createStatement();
                            statement.setQueryTimeout(30);
                            ResultSet resultSet = statement.executeQuery(sql);
                            Integer count = resultSet.next() ? VerifyResultStatusEnum.LEAKED.getCode()
                                : VerifyResultStatusEnum.NORMAL.getCode();
                            logger.info("sql漏数验证结果:[jdbcUrl:{},sql:{},count:{}]", jdbcUrl, sql, count);
                            resultMap.put(key, count);
                            resultSet.close();
                            statement.close();
                        } catch (SQLException throwables) {
                            resultMap.put(key, VerifyResultStatusEnum.FAILED.getCode());
                            logger.error("Verify job run error:", throwables);
                        }
                    });
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            logger.error("error:", e);
                        }
                    }
                }
            });
        } else {
            logger.warn("漏数验证配置为空，不予检测");
        }
        return resultMap;
    }
}
