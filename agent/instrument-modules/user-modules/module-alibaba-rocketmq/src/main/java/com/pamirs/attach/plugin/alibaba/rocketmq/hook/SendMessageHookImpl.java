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
package com.pamirs.attach.plugin.alibaba.rocketmq.hook;


import com.alibaba.rocketmq.client.hook.SendMessageContext;
import com.alibaba.rocketmq.client.hook.SendMessageHook;
import com.alibaba.rocketmq.client.impl.CommunicationMode;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageAccessor;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceBean;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceConstants;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceContext;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQType;
import com.pamirs.attach.plugin.alibaba.rocketmq.pub.MQSendMessageTraceLog;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 生产消息时的pradar埋点
 */
public class SendMessageHookImpl implements SendMessageHook, MQTraceConstants {
    private final static Logger LOGGER = LoggerFactory.getLogger(SendMessageHookImpl.class.getName());

    @Override
    public String hookName() {
        return "PradarSendMessageHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        try {
            if (context == null || context.getMessage() == null) {
                return;
            }
            Message message = context.getMessage();
            if (message == null) {
                return;
            }
            ClusterTestUtils.validateClusterTest();
            MQTraceBean traceBean = new MQTraceBean();
            traceBean.setTopic(message.getTopic());
            traceBean.setOriginMsgId(MessageAccessor.getOriginMessageId(message));
            traceBean.setTags(message.getTags());
            traceBean.setKeys(message.getKeys());
            traceBean.setBuyerId(message.getBuyerId());
            traceBean.setTransferFlag(MessageAccessor.getTransferFlag(message));
            traceBean.setCorrectionFlag(MessageAccessor.getCorrectionFlag(message));
            traceBean.setBodyLength(message.getBody().length);
            traceBean.setBornHost(context.getBornHost());
            String brokerAddr = context.getBrokerAddr();
            String port = "";
            if (context.getBrokerAddr() != null && context.getBrokerAddr().indexOf(':') != -1) {
                brokerAddr = brokerAddr.substring(0, brokerAddr.indexOf(':'));
                port = context.getBrokerAddr().substring(context.getBrokerAddr().indexOf(':') + 1);
            }
            traceBean.setStoreHost(brokerAddr);
            traceBean.setPort(port);

            traceBean.setBrokerName(context.getMq().getBrokerName());
            //上游AppName，记录当前应用的AppName
            traceBean.setProps(context.getProps());

            ArrayList<MQTraceBean> traceBeans = new ArrayList<MQTraceBean>(1);
            traceBeans.add(traceBean);

            MQTraceContext mqTraceContext = new MQTraceContext();
            context.setMqTraceContext(mqTraceContext);
            mqTraceContext.setMqType(MQType.ROCKETMQ);
            mqTraceContext.setTopic(context.getMq().getTopic());
            mqTraceContext.setGroup(context.getProducerGroup());
            mqTraceContext.setAsync(CommunicationMode.ASYNC == context.getCommunicationMode());
            mqTraceContext.setTraceBeans(traceBeans);
            Map<String, String> rpcContext = new HashMap<String, String>();
            for (String key : Pradar.getInvokeContextTransformKeys()) {
                String value = message.getProperty(key);
                if (value != null) {
                    rpcContext.put(key, value);
                }
            }
            traceBean.setContext(rpcContext);
            traceBean.setClusterTest(message.getUserProperty(PradarService.PRADAR_CLUSTER_TEST_KEY));

            MQSendMessageTraceLog.sendMessageBefore(mqTraceContext);

            for (Map.Entry<String, String> entry : traceBean.getContext().entrySet()) {
                putUserProperty(message, entry.getKey(), entry.getValue());
            }
        } catch (PradarException e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (PressureMeasureError e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(e);
            }
        }
    }

    private void putUserProperty(Message message, String key, String value) {
        if (value != null && !value.isEmpty()) {
            message.putUserProperty(key, value);
        }
    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {
        try {
            if (context == null || context.getMessage() == null) {
                return;
            }
            MQTraceContext mqTraceContext = (MQTraceContext) context.getMqTraceContext();
            MQTraceBean traceBean = mqTraceContext.getTraceBeans().get(0);

            if (traceBean != null && context.getSendResult() != null) {
                traceBean.setQueueId(context.getMq().getQueueId());
                traceBean.setMsgId(context.getSendResult().getMsgId());
                traceBean.setOffset(context.getSendResult().getQueueOffset());
                mqTraceContext.setSuccess(true);
                mqTraceContext.setStatus(context.getSendResult().getSendStatus().toString());
            } else {
                if (context.getException() != null) {
                    String msg = context.getException().getMessage();
                    mqTraceContext.setErrorMsg(StringUtils.substring(msg, 0, msg.indexOf("\n")));
                }
            }
            MQSendMessageTraceLog.sendMessageAfter(mqTraceContext);
        } catch (PradarException e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (PressureMeasureError e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(e);
            }
        }
    }
}
