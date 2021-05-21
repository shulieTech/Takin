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
package com.pamirs.pradar;

import com.pamirs.pradar.json.ResultSerializer;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pradar 日志格式编码，为针对异步队列调用做优化，不允许多线程并发调用
 */
abstract class BaseContextEncoder {
    public abstract void encode(BaseContext ctx, PradarAppender appender) throws IOException;
}

/**
 * Pradar 调用日志的输出
 */
class DefaultInvokeContextEncoder extends BaseContextEncoder {

    private int DEFAULT_BUFFER_SIZE = 256;
    private StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);


    private List<String> traceForUnitTest = new ArrayList<String>(100);


    @Override
    public void encode(BaseContext base, PradarAppender eea) throws IOException {
        AbstractContext ctx;
        if (base instanceof AbstractContext) {
            ctx = (AbstractContext) base;
        } else {
            return;
        }

        //Pradar TODO
        StringBuilder buffer = this.buffer;
        buffer.delete(0, buffer.length());
        buffer.append(ctx.getTraceId() == null ? "" : ctx.getTraceId()).append('|')
                .append(ctx.getStartTime()).append('|')
                .append(Pradar.AGENT_ID).append('|')
                .append(ctx.getTraceNode()).append('|')
                .append(ctx.getNodeId()).append('|')
                .append(ctx.getInvokeId() == null ? "" : ctx.getInvokeId()).append('|')
                .append(ctx.getLogType()).append('|')
                .append(ctx.getInvokeType()).append('|')
                .append(PradarCoreUtils.makeLogSafe(AppNameUtils.appName())).append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getTraceAppName() == null ? "" : ctx.getTraceAppName())).append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getUpAppName() == null ? "" : ctx.getUpAppName())).append('|')
                .append(ctx.getLogTime() - ctx.getStartTime()).append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getMiddlewareName() == null ? "" : ctx.getMiddlewareName())).append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getServiceName() == null ? "" : ctx.getServiceName())).append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getMethodName() == null ? "" : ctx.getMethodName())).append('|')
                .append(ctx.getRemoteIp() == null ? "" : ctx.getRemoteIp()).append('|')
                .append(ctx.getPort()).append('|')
                .append(ctx.getResultCode() == null ? "" : ctx.getResultCode()).append('|')
                .append(ctx.getRequestSize() < 0 ? 0 : ctx.getRequestSize()).append('|')
                .append(ctx.getResponseSize() < 0 ? 0 : ctx.getResponseSize()).append('|')
                .append(PradarCoreUtils.makeLogSafe(ResultSerializer.serializeRequest(ctx.getRequest() == null ? "" : ctx.getRequest(), Pradar.getPluginRequestSize()))).append('|')
                .append(PradarCoreUtils.makeLogSafe(ResultSerializer.serializeResponse(ctx.getResponse() == null ? "" : ctx.getResponse(), Pradar.getPluginResponseSize()))).append('|')
                .append(ctx.isClusterTest() ? '1' : '0').append('|')
                .append(PradarCoreUtils.makeLogSafe(ctx.getCallBackMsg() == null ? "" : ctx.getCallBackMsg()));

        final int samplingInterval = PradarSwitcher.getSamplingInterval();
        if (samplingInterval >= 2 && samplingInterval <= 9999) {
            buffer.append("|#").append(samplingInterval);
        }

        ctx.logContextData(buffer);
        buffer.append(PradarCoreUtils.NEWLINE);
        eea.append(buffer.toString());

        if (!StringUtils.isEmpty(System.getProperty("pradar.UnitTest"))) {
            if (traceForUnitTest.size() > 98) {
                traceForUnitTest.clear();
            }
            traceForUnitTest.add(buffer.toString());
        }
        ctx.destroy();
    }
}
