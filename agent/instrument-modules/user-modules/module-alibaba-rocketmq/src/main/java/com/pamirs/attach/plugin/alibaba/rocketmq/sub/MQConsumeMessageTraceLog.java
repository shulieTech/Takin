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
package com.pamirs.attach.plugin.alibaba.rocketmq.sub;

import com.pamirs.attach.plugin.alibaba.rocketmq.RocketmqConstants;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceBean;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceContext;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQType;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.PradarLogUtils;
import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.pressurement.ClusterTestUtils;

/**
 * 消息消费轨迹埋点
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/09/29
 */
public class MQConsumeMessageTraceLog {

    public static void consumeMessageBefore(MQTraceContext ctx) {

        if (ctx == null || ctx.getTraceBeans() == null || ctx.getTraceBeans().size() == 0
                || ctx.getTraceBeans().get(0) == null) {
            return;
        }

        // 记录第一条消息处理开始的时间
        ctx.setStartTime(System.currentTimeMillis());

        // 记录第一条消息开始消费
        // 由于是批量处理，没办法区分消费的调用具体属于哪个消息的调用链
        // 因此，后续消费的调用挂在第一条消息所在的调用链上
        MQTraceBean MQTraceBean = ctx.getTraceBeans().get(0);
        recordSingleMsgBeforeConsume(ctx, MQTraceBean, null);
        ClusterTestUtils.validateClusterTest();
    }

    public static void consumeMessageAfter(MQTraceContext ctx) {
        if (ctx == null || ctx.getTraceBeans() == null || ctx.getTraceBeans().size() == 0) {
            return;
        }

        // 批量消息全部处理完毕的时间
        long currentTime = System.currentTimeMillis();

        // 批量消息全部处理完毕的平均耗时
        long costTime = (currentTime - ctx.getStartTime()) / ctx.getTraceBeans().size();
        ctx.setCostTime(costTime);

        String firstTrace = Pradar.getTraceId();

        // 记录第一条消息结束消费,默认配置一次只消费一条消息时本方法到此结束
        if (MQType.ROCKETMQ.equals(ctx.getMqType())) {
            Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_SUCCESS, RocketmqConstants.PLUGIN_TYPE);
        }

        // 如果是批量消息需要特殊处理，后面只是记录一个处理结束的点
        if (ctx.getTraceBeans().size() > 1) {
            // 批量埋点,因为第一条消息结束消费时已记录，所以从第二条消息开始记录
            for (int i = 1; i < ctx.getTraceBeans().size(); i++) {
                MQTraceBean MQTraceBean = ctx.getTraceBeans().get(i);

                recordSingleMsgBeforeConsume(ctx, MQTraceBean, firstTrace);
                Pradar.endServerInvoke(ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_MQ);
            }
        }
    }

    private static void recordSingleMsgBeforeConsume(MQTraceContext ctx, MQTraceBean traceBean, String firstTrace) {
        String service = PradarLogUtils.getService(traceBean);
        String method = ctx.getGroup();

        Pradar.startServerInvoke(service, method, traceBean.getContext());
        Pradar.middlewareName(RocketmqConstants.PLUGIN_NAME);

        if (firstTrace != null) {
            // 追加 msgId 和第一条消息的 TraceId
            Pradar.callBack(traceBean.getMsgId() + "/" + firstTrace);
        } else {
            Pradar.callBack(traceBean.getMsgId());
        }
        Pradar.remoteIp(traceBean.getStoreHost());
        Pradar.remotePort(traceBean.getPort());
        Pradar.responseSize(traceBean.getBodyLength());
        Pradar.startTime(ctx.getStartTime());
    }

}
