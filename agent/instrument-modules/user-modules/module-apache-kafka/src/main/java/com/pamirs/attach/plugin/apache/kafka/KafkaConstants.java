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
package com.pamirs.attach.plugin.apache.kafka;

import com.pamirs.pradar.MiddlewareType;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/10 5:29 下午
 */
public class KafkaConstants {
    public final static String UNKNOWN = "unknow";
    public final static String PLUGIN_NAME = "apache-kafka";
    public final static int PLUGIN_TYPE = MiddlewareType.TYPE_MQ;

    public final static String MODULE_NAME = "apache-kafka";

    public final static String DYNAMIC_FIELD_REMOTE_ADDRESS = "remoteAddress";
    public final static String DYNAMIC_FIELD_GROUP = "group";

    public final static String DYNAMIC_FIELD_PT_OBJECT = "ptObject";
    public final static String DYNAMIC_FIELD_CONSUMER_FACTORY = "consumerFactory";
    public final static String DYNAMIC_FIELD_CONTAINER_CLASS = "containerClass";

    public final static String REFLECT_FIELD_CONTAINER_PROPERTIES = "containerProperties";
    public final static String REFLECT_FIELD_CONTAINER = "container";
    public final static String REFLECT_FIELD_THIS_OR_PARENT_CONTAINER = "thisOrParentContainer";
    public final static String REFLECT_FIELD_TOPICS = "topics";
    public final static String REFLECT_FIELD_TOPIC = "topic";
    public final static String REFLECT_FIELD_CONSUMER_FACTORY = "consumerFactory";
    public final static String REFLECT_FIELD_PRODUCER_CONFIG = "producerConfig";

    public final static String REFLECT_FIELD_CONSUMER = "consumer";
    public final static String REFLECT_FIELD_GROUP_ID = "groupId";
    public final static String REFLECT_FIELD_COORDINATOR = "coordinator";


    public final static String REFLECT_METHOD_GET_MESSAGE_LISTENER = "getMessageListener";
    public final static String REFLECT_METHOD_SET_MESSAGE_LISTENER = "setMessageListener";

    public final static String REFLECT_METHOD_GET_CONTAINER_PROPERTIES = "getContainerProperties";

    public final static String REFLECT_METHOD_GET_ACK_TIME = "getAckTime";
    public final static String REFLECT_METHOD_SET_ACK_TIME = "setAckTime";

    public final static String REFLECT_METHOD_GET_GROUP_ID = "getGroupId";
    public final static String REFLECT_METHOD_SET_GROUP_ID = "setGroupId";

    public final static String REFLECT_METHOD_GET_ACK_MODE = "getAckMode";
    public final static String REFLECT_METHOD_SET_ACK_MODE = "setAckMode";

    public final static String REFLECT_METHOD_GET_POLL_TIMEOUT = "getPollTimeout";
    public final static String REFLECT_METHOD_SET_POLL_TIMEOUT = "setPollTimeout";

    public final static String REFLECT_METHOD_GET_TOPICS = "getTopics";
    public final static String REFLECT_METHOD_GET_CONCURRENCY = "getConcurrency";

    public final static String REFLECT_METHOD_STOP = "stop";

    public static final String KEY_BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String KEY_ZOOKEEPER_CONNECT = "zookeeper.connect";
}
