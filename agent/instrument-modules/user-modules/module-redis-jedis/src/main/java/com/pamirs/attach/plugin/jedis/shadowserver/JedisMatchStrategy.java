/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.jedis.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisServerMatchStrategy;
import com.pamirs.attach.plugin.common.datasource.redisserver.RedisServerNodesStrategy;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.jedis.shadowserver
 * @Date 2020/12/2 5:13 下午
 */
public class JedisMatchStrategy implements RedisServerMatchStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(JedisMatchStrategy.class);

    private final RedisServerNodesStrategy strategy;

    public JedisMatchStrategy(RedisServerNodesStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 这里保证JedisCluster不能通过此校验方式。该校验规则给Jedis MasterSlave使用
     *
     * @param obj 任意对象
     * @return
     */
    @Override
    public ShadowRedisConfig getConfig(Object obj) {

        if (GlobalConfig.getInstance().getShadowRedisConfigs().size() > 0 && obj != null) {

            try {
                List<String> nodes = strategy.match(obj);
                for (String configKey : GlobalConfig.getInstance().getShadowRedisConfigs().keySet()) {
                    List<String> configKeys = configKey.contains(",")
                            ? Arrays.asList(StringUtils.split(configKey, ','))
                            : Collections.singletonList(configKey);

                    // 这里保证JedisCluster不能通过此校验方式。该校验规则给Jedis MasterSlave使用
                    ShadowRedisConfig config = GlobalConfig.getInstance().getShadowRedisConfig(configKey);
                    if (StringUtils.isNotBlank(config.getMaster())) {
                        for (String key : nodes) {
                            if (configKeys.contains(key)) {
                                return GlobalConfig.getInstance().getShadowRedisConfig(configKey);
                            }
                        }
                    } else {
                        int count = 0;
                        for (String key : nodes) {
                            if (!configKeys.contains(key)) {
                                count = 0;
                                break;
                            }
                            count++;
                        }
                        if (count == nodes.size() && configKeys.size() == count) {
                            return GlobalConfig.getInstance().getShadowRedisConfig(configKey);
                        }
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("", e);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.RedisServer)
                        .setErrorCode("redisServer-0001")
                        .setMessage("获取影子数据源失败！")
                        .setDetail(ExceptionUtils.getStackTrace(e))
                        .report();
            }
        }

        return null;
    }
}
