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
package com.shulie.instrument.simulator.module.tomcat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.metrics.SumRateCounter;
import com.shulie.instrument.simulator.module.model.tomcat.TomcatInfo;
import com.shulie.instrument.simulator.module.util.NetUtils;
import org.kohsuke.MetaInfServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:37 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "tomcat", version = "1.0.0", author = "xiaobin@shulie.io", description = "tomcat 模块")
public class TomcatModule extends ParamSupported implements ExtensionModule {

    private SumRateCounter tomcatRequestCounter = new SumRateCounter();
    private SumRateCounter tomcatErrorCounter = new SumRateCounter();
    private SumRateCounter tomcatReceivedBytesCounter = new SumRateCounter();
    private SumRateCounter tomcatSentBytesCounter = new SumRateCounter();

    @Command(value = "info", description = "查看 tomcat 信息")
    public CommandResponse info(final Map<String, String> args) {
        try {
            int port = ParameterUtils.getInt(args, "port", 8006);
            // 如果请求tomcat信息失败，则不显示tomcat信息
            final String url = "http://localhost:" + port;
            if (!NetUtils.request(url).isSuccess()) {
                return CommandResponse.failure("tomcat unreachable with port:" + url);
            }

            TomcatInfo tomcatInfo = new TomcatInfo();
            String threadPoolPath = url + "/connector/threadpool";
            String connectorStatPath = url + "/connector/stats";
            NetUtils.Response connectorStatResponse = NetUtils.request(connectorStatPath);
            if (connectorStatResponse.isSuccess()) {
                List<TomcatInfo.ConnectorStats> connectorStats = new ArrayList<TomcatInfo.ConnectorStats>();
                List<JSONObject> tomcatConnectorStats = JSON.parseArray(connectorStatResponse.getContent(), JSONObject.class);
                for (JSONObject stat : tomcatConnectorStats) {
                    String connectorName = stat.getString("name").replace("\"", "");
                    long bytesReceived = stat.getLongValue("bytesReceived");
                    long bytesSent = stat.getLongValue("bytesSent");
                    long processingTime = stat.getLongValue("processingTime");
                    long requestCount = stat.getLongValue("requestCount");
                    long errorCount = stat.getLongValue("errorCount");

                    tomcatRequestCounter.update(requestCount);
                    tomcatErrorCounter.update(errorCount);
                    tomcatReceivedBytesCounter.update(bytesReceived);
                    tomcatSentBytesCounter.update(bytesSent);

                    double qps = tomcatRequestCounter.rate();
                    double rt = processingTime / (double) requestCount;
                    double errorRate = tomcatErrorCounter.rate();
                    long receivedBytesRate = new Double(tomcatReceivedBytesCounter.rate()).longValue();
                    long sentBytesRate = new Double(tomcatSentBytesCounter.rate()).longValue();

                    TomcatInfo.ConnectorStats connectorStat = new TomcatInfo.ConnectorStats();
                    connectorStat.setName(connectorName);
                    connectorStat.setQps(qps);
                    connectorStat.setRt(rt);
                    connectorStat.setError(errorRate);
                    connectorStat.setReceived(receivedBytesRate);
                    connectorStat.setSent(sentBytesRate);
                    connectorStats.add(connectorStat);
                }
                tomcatInfo.setConnectorStats(connectorStats);
            }

            NetUtils.Response threadPoolResponse = NetUtils.request(threadPoolPath);
            if (threadPoolResponse.isSuccess()) {
                List<TomcatInfo.ThreadPool> threadPools = new ArrayList<TomcatInfo.ThreadPool>();
                List<JSONObject> threadPoolInfos = JSON.parseArray(threadPoolResponse.getContent(), JSONObject.class);
                for (JSONObject info : threadPoolInfos) {
                    String name = info.getString("name").replace("\"", "");
                    long busy = info.getLongValue("threadBusy");
                    long total = info.getLongValue("threadCount");
                    threadPools.add(new TomcatInfo.ThreadPool(name, busy, total));
                }
                tomcatInfo.setThreadPools(threadPools);
            }
            return CommandResponse.success(tomcatInfo);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }
}
