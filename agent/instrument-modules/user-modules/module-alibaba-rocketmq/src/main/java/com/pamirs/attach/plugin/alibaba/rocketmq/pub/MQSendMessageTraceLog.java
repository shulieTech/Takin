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
package com.pamirs.attach.plugin.alibaba.rocketmq.pub;

import com.pamirs.attach.plugin.alibaba.rocketmq.RocketmqConstants;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceBean;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.MQTraceContext;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.PradarLogUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;

/**
 * 消息发送轨迹埋点
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/09/29
 */
public class MQSendMessageTraceLog {

    public static void sendMessageBefore(MQTraceContext ctx) {
        if (ctx == null || ctx.getTraceBeans() == null || ctx.getTraceBeans().size() == 0
                || ctx.getTraceBeans().get(0) == null) {
            return;
        }

        String service = PradarLogUtils.getService(ctx);
        String method = PradarLogUtils.getMethod(ctx);
        Pradar.startClientInvoke(service, method);

        MQTraceBean traceBean = ctx.getTraceBeans().get(0);
        traceBean.setContext(Pradar.getInvokeContextMap());
        //如果使用消息头模式传递压测标，则需要显示从消费发送端设置

        Pradar.requestSize(traceBean.getBodyLength());
        Pradar.remoteIp(traceBean.getStoreHost());
        Pradar.remotePort(traceBean.getPort());
        Pradar.middlewareName(RocketmqConstants.PLUGIN_NAME);

        // 如果采用异步的方式提交消息，需要将Pradar的context在不同线程中进行传递（默认Pradar的context是存放在
        // ThreadLocal 中）
        if (ctx.isAsync()) {
            ctx.setRpcContextInner(Pradar.getInvokeContextMap());
            Pradar.popInvokeContext();
        }
    }

    public static void sendMessageAfter(MQTraceContext ctx) {
        if (ctx == null || ctx.getTraceBeans() == null || ctx.getTraceBeans().size() == 0
                || ctx.getTraceBeans().get(0) == null) {
            return;
        }

        // 批量消息全部处理完毕的平均耗时
        long costTime = (System.currentTimeMillis() - ctx.getStartTime()) / ctx.getTraceBeans().size();
        ctx.setCostTime(costTime);

        // 如果采用异步的方式提交消息，需要将 Pradar 的 context 在不同线程中进行传递
        // （默认 Pradar 的 context 是存放在 ThreadLocal 中）
        if (ctx.isAsync()) {
            Pradar.setInvokeContext(ctx.getRpcContextInner());
        }

        Pradar.remoteIp(ctx.getTraceBeans().get(0).getStoreHost());
        Pradar.remotePort(ctx.getTraceBeans().get(0).getPort());
        Pradar.request(ctx.getTraceBeans());
        if (!ctx.isSuccess()) {
            Pradar.response(ctx.getErrorMsg());
            Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_FAILED, RocketmqConstants.PLUGIN_TYPE);
            return;
        }

        // 消息发送成功后追加MsgId
        if (Pradar.isResponseOn()) {
            Pradar.response(ctx.getTraceBeans().get(0).getMsgId());
        }
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_SUCCESS, RocketmqConstants.PLUGIN_TYPE);
    }
}
