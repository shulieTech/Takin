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
package com.pamirs.attach.plugin.apache.kafka.interceptor;

import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.apache.commons.lang.ArrayUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import javax.annotation.Resource;
import java.util.List;

/**
 * 这个一般不一定能执行到，因为探针后续执行，可能 KafkaConsumer 的构造方法已经执行完了，所以
 * 在 Consumer 里面去获取地址和 groupId 则不能靠这种方式
 *
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2019-08-05 19:32
 */
public class ConsumerConstructorInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doAfter(Advice advice) {
        ConsumerConfig consumerConfig = getConsumerConfig(advice.getParameterArray());
        if (consumerConfig == null) {
            return;
        }
        String remoteAddress = getRemoteAddress(consumerConfig);
        manager.setDynamicField(advice.getTarget(), KafkaConstants.DYNAMIC_FIELD_REMOTE_ADDRESS, remoteAddress);

        String group = getGroup(consumerConfig);
        manager.setDynamicField(advice.getTarget(), KafkaConstants.DYNAMIC_FIELD_GROUP, group);

    }

    private ConsumerConfig getConsumerConfig(Object args[]) {
        if (ArrayUtils.isEmpty(args)) {
            return null;
        }

        if (args[0] instanceof ConsumerConfig) {
            return (ConsumerConfig) args[0];
        }

        return null;
    }

    private String getListString(List<String> value) {
        if (value != null) {
            if (value.size() == 1) {
                return value.get(0);
            } else {
                StringBuilder builder = new StringBuilder();
                for (String str : value) {
                    builder.append(str).append(',');
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                return builder.toString();
            }
        }
        return "";
    }

    private String getValue(ConsumerConfig consumerConfig, String key) {
        try {
            List<String> value = consumerConfig.getList(key);
            return getListString(value);
        } catch (Throwable e) {
            return consumerConfig.getString(key);
        }
    }

    private String getRemoteAddress(ConsumerConfig consumerConfig) {
        try {
            String value = getValue(consumerConfig, KafkaConstants.KEY_BOOTSTRAP_SERVERS);
            if (value == null) {
                value = getValue(consumerConfig, KafkaConstants.KEY_ZOOKEEPER_CONNECT);
            }
            if (value == null) {
                return KafkaConstants.UNKNOWN;
            }
            return value;
        } catch (Throwable e) {
            return KafkaConstants.UNKNOWN;
        }
    }

    private String getGroup(ConsumerConfig consumerConfig) {
        String groupId = consumerConfig.getString("group.id");
        return groupId;
    }


}
