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
package com.pamirs.attach.plugin.common.datasource.redisserver;

import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.common.redisserver
 * @Date 2020/11/26 2:29 下午
 */
public class DefaultRedisServerMatch implements RedisServerMatchStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultRedisServerMatch.class);

    private final RedisServerNodesStrategy strategy;

    public DefaultRedisServerMatch(RedisServerNodesStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 全部匹配
     * 需要考虑一种情况，在控制台中配置的是ip:port
     * 从redis客户端获取也是一组IP列表，没法保证他们是顺序的
     * 所以需要遍历每个node(ip:port)进行匹配，每个node都匹配成功才算
     *
     * @param obj
     * @return
     */
    @Override
    public ShadowRedisConfig getConfig(Object obj) {

        List<String> nodes = null;
        if (GlobalConfig.getInstance().getShadowRedisConfigs().size() > 0 && obj != null) {

            try {
                nodes = strategy.match(obj);
                int count = 0;
                for (String configKey : GlobalConfig.getInstance().getShadowRedisConfigs().keySet()) {
                    List<String> configKeys = configKey.contains(",")
                            ? Arrays.asList(StringUtils.split(configKey,','))
                            : Arrays.asList(configKey);

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

        throw new PressureMeasureError("not found redis shadow server config error." + convertMessage(nodes));
    }

    public String collectionToString(Collection<String> nodes) {
        StringBuilder builder = new StringBuilder();
        for (String node : nodes) {
            builder.append(node).append(",");
        }
        return builder.toString();
    }

    public String convertMessage(List<String> nodes) {
        return String.format("客户端nodes:%s, 控制台配置nodes:%s", collectionToString(nodes), collectionToString(GlobalConfig.getInstance().getShadowRedisConfigs().keySet()));
    }
}
