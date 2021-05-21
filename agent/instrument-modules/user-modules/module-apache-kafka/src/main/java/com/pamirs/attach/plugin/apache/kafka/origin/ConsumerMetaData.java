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
package com.pamirs.attach.plugin.apache.kafka.origin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.attach.plugin.apache.kafka.util.ReflectUtil;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Node;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/12 5:17 下午
 */
public class ConsumerMetaData {

    private final Set<String> topics;

    private final List<String> shadowTopics;

    private final String groupId;

    private final String ptGroupId;

    private final boolean hasShadow;

    private final String bootstrapServers;

    public ConsumerMetaData(Set<String> topics, String groupId, List<String> shadowTopics,
        String bootstrapServers) {
        this.topics = topics;
        this.groupId = groupId;
        this.ptGroupId = Pradar.addClusterTestPrefix(groupId);
        this.shadowTopics = shadowTopics;
        this.hasShadow = shadowTopics.size() > 0;
        this.bootstrapServers = bootstrapServers;
    }

    public static ConsumerMetaData build(KafkaConsumer consumer) {
        Set<String> topics = consumer.subscription();
        try {
            Object coordinator = Reflect.on(consumer).get(KafkaConstants.REFLECT_FIELD_COORDINATOR);
            String groupId = ReflectUtil.reflectSlience(consumer, KafkaConstants.REFLECT_FIELD_GROUP_ID);
            if (groupId == null) {
                groupId = ReflectUtil.reflectSlience(coordinator, KafkaConstants.REFLECT_FIELD_GROUP_ID);
                if (groupId == null) {
                    throw new PressureMeasureError("未支持的kafka版本！未能获取groupId");
                }
            }
            Object metadata = Reflect.on(consumer).get("metadata");
            List<String> shadowTopic = getShadowTopics(topics, groupId);

            Object cluster = ReflectUtil.reflectSlience(metadata, "cluster");
            Iterable<Node> nodes;
            if (cluster != null) {
                nodes = Reflect.on(cluster).get("nodes");
            } else {
                Object cache = ReflectUtil.reflectSlience(metadata, "cache");
                if (cache != null) {
                    nodes = Reflect.on(cache).get("nodes");
                } else {
                    throw new PressureMeasureError("未支持的kafka版本！未能获取nodes");
                }
            }
            StringBuilder sb = new StringBuilder();
            for (Node node : nodes) {
                sb.append(Reflect.on(node).get("host").toString()).append(":").append(Reflect.on(node).get("port")
                    .toString()).append(",");
            }
            return new ConsumerMetaData(topics, groupId, shadowTopic, sb.substring(0, sb.length() - 1));
        } catch (ReflectException e) {
            throw new PressureMeasureError(e);
        }
    }

    public static List<String> getShadowTopics(Set<String> topics, String groupId) {
        List<String> topicList = new ArrayList<String>();
        if (topics != null) {
            for (String topic : topics) {
                /**
                 * topic 都需要在白名单中配置好才可以启动
                 */
                if (StringUtils.isNotBlank(topic) && !Pradar.isClusterTestPrefix(topic)) {
                    if (GlobalConfig.getInstance().getMqWhiteList().contains(topic) || GlobalConfig.getInstance()
                        .getMqWhiteList().contains(topic + '#' + groupId)) {
                        topicList.add(Pradar.addClusterTestPrefix(topic));
                    }
                }
            }
        }
        return topicList;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public List<String> getShadowTopics() {
        return shadowTopics;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isHasShadow() {
        return hasShadow;
    }

    public String getPtGroupId() {
        return ptGroupId;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }
}
