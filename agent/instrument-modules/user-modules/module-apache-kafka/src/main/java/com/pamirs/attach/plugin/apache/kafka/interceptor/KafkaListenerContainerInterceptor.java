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

import com.pamirs.attach.plugin.apache.kafka.ConfigCache;
import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.agent.shared.util.PradarSpringUtil;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.shulie.instrument.simulator.api.resource.ModuleController;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建影子Consumer
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.interceptor
 * @Date 2020-04-03 16:47
 */
public class KafkaListenerContainerInterceptor extends AroundInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(KafkaListenerContainerInterceptor.class.getName());

    @Resource
    private ModuleController moduleController;

    @Override
    public void doBefore(Advice advice) {
        if (PradarSpringUtil.getBeanFactory() == null) {
            return;
        }
        Object thisObj = advice.getTarget();
        /**
         * 如果已经初始化过了，则忽略
         */
        if (ConfigCache.isInited(thisObj)) {
            return;
        }
        Object externalContainer = null;
        try {
            externalContainer = Reflect.on(thisObj).get("this$0");
        } catch (ReflectException e) {
            logger.warn("SIMULATOR: kafka consumer register error. can't found field this$0. {}", thisObj.getClass().getName());
            return;
        }
        if (externalContainer == null) {
            logger.warn("SIMULATOR: kafka consumer register error. field this$0 is null. {}", thisObj.getClass().getName());
            return;
        }

        Object container = null;
        try {
            container = Reflect.on(externalContainer).get(KafkaConstants.REFLECT_FIELD_THIS_OR_PARENT_CONTAINER);
        } catch (ReflectException e) {
        }

        if (container == null) {
            try {
                container = Reflect.on(externalContainer).get(KafkaConstants.REFLECT_FIELD_CONTAINER);
            } catch (ReflectException e) {
            }
        }

        if (container == null) {
            logger.warn("SIMULATOR: kafka consumer register error. field {} is null. {}", KafkaConstants.REFLECT_FIELD_CONTAINER, externalContainer.getClass().getName());
            return;
        }

        Object containerProperties = null;
        try {
            containerProperties = Reflect.on(container).get(KafkaConstants.REFLECT_FIELD_CONTAINER_PROPERTIES);
        } catch (ReflectException e) {
        }
        if (containerProperties == null) {
            try {
                containerProperties = Reflect.on(container).call(KafkaConstants.REFLECT_METHOD_GET_CONTAINER_PROPERTIES).get();
            } catch (ReflectException e) {
            }
        }

        if (containerProperties == null) {
            logger.warn("SIMULATOR: kafka consumer register error. got a null containerProperties from {}.", container.getClass().getName());
            return;
        }

        String groupId = null;
        try {
            groupId = Reflect.on(containerProperties).call(KafkaConstants.REFLECT_METHOD_GET_GROUP_ID).get();
        } catch (ReflectException e) {
        }
        /**
         * 如果是影子 topic，则不需要再创建对应的消费者
         */
        List<String> topicList = getShadowTopics(containerProperties, groupId);
        if (CollectionUtils.isEmpty(topicList)) {
            return;
        }
        Object messageListener = null;
        try {
            messageListener = Reflect.on(containerProperties).call(KafkaConstants.REFLECT_METHOD_GET_MESSAGE_LISTENER).get();
        } catch (ReflectException e) {
        }

        if (null == messageListener) {
            logger.warn("SIMULATOR: kafka consumer register error. got a null messageListener from {}.", containerProperties);
            return;
        }

        Object ptObject = null;
        try {
            ptObject = Reflect.on(containerProperties.getClass()).create(new Object[]{topicList.toArray(new String[0])}).get();
        } catch (ReflectException e) {
        }
        if (null == ptObject) {
            return;
        }

        // 设置业务Consumer属性
        setContainerProperties(ptObject, containerProperties);
        try {
            Reflect.on(ptObject).call(KafkaConstants.REFLECT_METHOD_SET_MESSAGE_LISTENER, messageListener);
        } catch (ReflectException e) {
        }

        final DefaultListableBeanFactory defaultListableBeanFactory = PradarSpringUtil.getBeanFactory();
        // 获取bean工厂并转换为DefaultListableBeanFactory
        final String beanName = toShadowTopicString(topicList) + container.getClass().getSimpleName();
        int concurrency = 0;
        try {
            concurrency = Reflect.on(container).call(KafkaConstants.REFLECT_METHOD_GET_CONCURRENCY).get();
        } catch (ReflectException e) {
        }

        Object consumerFactory = null;
        try {
            consumerFactory = Reflect.on(container).get(KafkaConstants.REFLECT_FIELD_CONSUMER_FACTORY);
        } catch (ReflectException e) {
        }
        if (consumerFactory == null) {
            logger.warn("SIMULATOR: kafka consumer register error. got a null consumerFactory from {}.", container);
            return;
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(container.getClass())
                .setInitMethodName("doStart")
                .addConstructorArgValue(consumerFactory)
                .addConstructorArgValue(ptObject)
                .addPropertyValue("concurrency", concurrency);

        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

        /**
         * 添加释放资源,会在模块卸载的时候调用
         */
        moduleController.addReleaseResource(new ReleaseResource<Object>(null) {
            @Override
            public void release() {
                Object bean = defaultListableBeanFactory.getBean(beanName);
                if (bean != null) {
                    try {
                        Reflect.on(bean).call(KafkaConstants.REFLECT_METHOD_STOP);
                    } catch (ReflectException e) {
                    }
                }
                defaultListableBeanFactory.removeBeanDefinition(beanName);
            }
        });
        PradarSpringUtil.getBeanFactory().getBean(beanName);
    }

    private String toShadowTopicString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str).append('_');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * 设置 ContainerProperties 的属性
     *
     * @param ptContainerProperties
     * @param orgContainerProperties
     */
    private void setContainerProperties(Object ptContainerProperties, Object orgContainerProperties) {
        //不管高低版本，全部采用反射的方式
        // 设置AckTime 属性
        Object ackTime = null;
        try {
            ackTime = Reflect.on(orgContainerProperties).call(KafkaConstants.REFLECT_METHOD_GET_ACK_TIME).get();
        } catch (ReflectException e) {
        }
        if (ackTime != null) {
            long ackTimeLong = Long.valueOf(String.valueOf(ackTime));
            try {
                Reflect.on(ptContainerProperties).call(KafkaConstants.REFLECT_METHOD_SET_ACK_TIME, ackTimeLong);
            } catch (ReflectException e) {
            }
        }

        // 设置groupid 属性
        String groupId = null;
        try {
            groupId = Reflect.on(orgContainerProperties).call(KafkaConstants.REFLECT_METHOD_GET_GROUP_ID).get();
        } catch (ReflectException e) {
        }

        if (groupId != null) {
            try {
                Reflect.on(ptContainerProperties).call(KafkaConstants.REFLECT_METHOD_SET_GROUP_ID, Pradar.addClusterTestPrefix(groupId));
            } catch (ReflectException e) {
            }
        }

        // 设置AckMode 属性
        Object ackMode = null;
        try {
            ackMode = Reflect.on(orgContainerProperties).call(KafkaConstants.REFLECT_METHOD_GET_ACK_MODE).get();
        } catch (ReflectException e) {
        }

        if (ackMode != null) {
            try {
                Reflect.on(ptContainerProperties).call(KafkaConstants.REFLECT_METHOD_SET_ACK_MODE, ackMode);
            } catch (ReflectException e) {
            }
        }

        // 设置AckMode 属性
        Long pollTimeout = null;
        try {
            pollTimeout = Reflect.on(orgContainerProperties).call(KafkaConstants.REFLECT_METHOD_GET_POLL_TIMEOUT).get();
        } catch (ReflectException e) {
        }

        // 设置PollTimeout 属性
        if (pollTimeout != null) {
            try {
                Reflect.on(ptContainerProperties).call(KafkaConstants.REFLECT_METHOD_SET_POLL_TIMEOUT, pollTimeout);
            } catch (ReflectException e) {
            }
        }
    }

    /**
     * 获取Topic
     *
     * @param object
     * @return
     */
    private List<String> getShadowTopics(Object object, String groupId) {
        List<String> topicList = new ArrayList<String>();
        String[] topics = null;
        try {
            topics = Reflect.on(object).call(KafkaConstants.REFLECT_METHOD_GET_TOPICS).get();
        } catch (ReflectException e) {
        }
        if (topics == null) {
            try {
                topics = Reflect.on(object).get(KafkaConstants.REFLECT_FIELD_TOPICS);
            } catch (ReflectException e) {
            }
        }
        if (topics != null) {
            for (String topic : topics) {
                /**
                 * topic 都需要在白名单中配置好才可以启动
                 */
                if (StringUtils.isNotBlank(topic) && !Pradar.isClusterTestPrefix(topic)) {
                    if (GlobalConfig.getInstance().getMqWhiteList().contains(topic) || GlobalConfig.getInstance().getMqWhiteList().contains(topic + '#' + groupId)) {
                        topicList.add(Pradar.addClusterTestPrefix(topic));
                    }
                }
            }
        }
        return topicList;
    }

    @Override
    public void doException(Advice advice) {
        Throwable throwable = advice.getThrowable();
        logger.error(throwable.getMessage(), throwable);
        ErrorReporter.buildError()
                .setErrorType(ErrorTypeEnum.MQ)
                .setErrorCode("MQ-0001")
                .setMessage("kafka-AbstractMessageListenerContainer启动失败！")
                .setDetail(throwable.getMessage())
                .report();

    }
}
