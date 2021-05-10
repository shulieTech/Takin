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

import com.alibaba.rocketmq.client.hook.ConsumeMessageContext;
import com.alibaba.rocketmq.client.hook.ConsumeMessageHook;
import com.alibaba.rocketmq.common.message.MessageAccessor;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceBean;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceConstants;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceContext;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQType;
import com.pamirs.attach.plugin.alibaba.rocketmq.sub.MQConsumeMessageTraceLog;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullConsumeMessageHookImpl implements ConsumeMessageHook, MQTraceConstants {
    private final static Logger LOGGER = LoggerFactory.getLogger(PullConsumeMessageHookImpl.class.getName());
    public static final String RETRYSTR = "%RETRY%";
    public static final String DLQSTR = "%DLQ%";

    @Override
    public String hookName() {
        return "PradarPullConsumeMessageHook";
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext context) {
        try {
            if (context == null
                    || context.getMsgList() == null || context.getMsgList().isEmpty()) {
                return;
            }
            MQTraceContext mqTraceContext = new MQTraceContext();
            context.setMqTraceContext(mqTraceContext);
            mqTraceContext.setMqType(MQType.ROCKETMQ);
            mqTraceContext.setTopic(context.getMq().getTopic());
            mqTraceContext.setGroup(context.getConsumerGroup());

            List<MQTraceBean> beans = new ArrayList<MQTraceBean>();
            for (MessageExt msg : context.getMsgList()) {
                if (msg == null) {
                    continue;
                }
                MQTraceBean traceBean = new MQTraceBean();
                Map<String, String> rpcContext = new HashMap<String, String>();
                for (String key : Pradar.getInvokeContextTransformKeys()) {
                    String value = msg.getProperty(key);
                    if(value != null) {
                        rpcContext.put(key,value);
                    }
                }
                traceBean.setContext(rpcContext);
                traceBean.setTopic(msg.getTopic());
                traceBean.setMsgId(msg.getMsgId());
                traceBean.setOriginMsgId(MessageAccessor.getOriginMessageId(msg));
                traceBean.setTags(msg.getTags());
                traceBean.setKeys(msg.getKeys());
                traceBean.setBuyerId(msg.getBuyerId());
                traceBean.setTransferFlag(MessageAccessor.getTransferFlag(msg));
                traceBean.setCorrectionFlag(MessageAccessor.getCorrectionFlag(msg));
                traceBean.setBodyLength(msg.getBody().length);
                traceBean.setBornHost(StringUtils.substring(msg.getBornHost().toString(), 1));
                String storeHost = "";
                String port = "";
                if (msg.getStoreHost() != null && msg.getStoreHost() instanceof InetSocketAddress) {
                    InetSocketAddress address = (InetSocketAddress) msg.getStoreHost();
                    storeHost = address.getAddress() == null ? null : address.getAddress().getHostAddress();
                    port = String.valueOf(address.getPort());
                } else {
                    storeHost = StringUtils.substring(msg.getStoreHost().toString(), 1);
                }
                traceBean.setStoreHost(storeHost);
                traceBean.setPort(port);
                traceBean.setStoreTime(msg.getStoreTimestamp());
                traceBean.setBrokerName(context.getMq().getBrokerName());
                traceBean.setQueueId(msg.getQueueId());
                traceBean.setOffset(msg.getQueueOffset());
                traceBean.setRetryTimes(msg.getReconsumeTimes());
                traceBean.setProps(context.getProps());
                boolean isClusterTest = msg.getTopic() != null &&
                        (Pradar.isClusterTestPrefix(msg.getTopic())
                                || Pradar.isClusterTestPrefix(msg.getTopic(), RETRYSTR)
                                || Pradar.isClusterTestPrefix(msg.getTopic(), DLQSTR));
                // 消息的properties是否包含Pradar.PRADAR_CLUSTER_TEST_KEY
                isClusterTest = isClusterTest || ClusterTestUtils.isClusterTestRequest(msg.getProperty(PradarService.PRADAR_CLUSTER_TEST_KEY));
                if (isClusterTest) {
                    traceBean.setClusterTest(Boolean.TRUE.toString());
                }
                beans.add(traceBean);
            }
            mqTraceContext.setTraceBeans(beans);
            MQConsumeMessageTraceLog.consumeMessageBefore(mqTraceContext);
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


    @Override
    public void consumeMessageAfter(ConsumeMessageContext context) {
        try {
            if (context == null || context.getMsgList() == null || context.getMsgList().isEmpty()) {
                return;
            }
            MQTraceContext mqTraceContext = (MQTraceContext) context.getMqTraceContext();
            mqTraceContext.setSuccess(context.isSuccess());
            mqTraceContext.setStatus(context.getStatus());
            MQConsumeMessageTraceLog.consumeMessageAfter(mqTraceContext);

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
