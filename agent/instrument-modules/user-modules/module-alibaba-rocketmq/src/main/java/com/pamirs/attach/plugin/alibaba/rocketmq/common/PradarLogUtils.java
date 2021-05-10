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
package com.pamirs.attach.plugin.alibaba.rocketmq.common;


/**
 * pradar 链路日志
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/09/29
 */
public class PradarLogUtils implements MQTraceConstants {
    public static String getMethod(MQTraceContext ctx, MQTraceBean traceBean) {
        StringBuilder sb = new StringBuilder(256);
        // 格式为TOPIC:CGROUP:TAGS:KEYS
        sb.append(traceBean.getTopic()).append(':').append(ctx.getGroup()).append(':')
                .append(MixUtils.replaceNull(traceBean.getTags()).replaceAll(TAG_SPLITOR, NEW_TAG_SPLITOR))
                .append(':')
                .append(MixUtils.replaceNull(traceBean.getKeys()));
        return sb.toString();
    }

    public static String getService(MQTraceContext ctx) {
        return ctx.getTraceBeans().get(0).getTopic();
    }

    public static String getService(MQTraceBean ctx) {
        return ctx.getTopic();
    }

    public static String getMethod(MQTraceContext ctx) {
        StringBuilder sb = new StringBuilder(256);
        // 格式为TOPIC:PGROUP:TAGS:KEYS
        MQTraceBean traceBean = ctx.getTraceBeans().get(0);
        sb.append(ctx.getGroup()).append(":")
                .append(MixUtils.replaceNull(traceBean.getTags()).replaceAll(TAG_SPLITOR, NEW_TAG_SPLITOR))
                .append(':')
                .append(MixUtils.replaceNull(traceBean.getKeys()));
        return sb.toString();
    }
}
